/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.cos.cas.adaptors.mongodb;

import org.jasig.cas.support.oauth.scope.Scope;
import org.jasig.cas.support.oauth.scope.handler.support.AbstractScopeHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.validation.constraints.NotNull;

public class OpenScienceFrameworkScopeHandler extends AbstractScopeHandler
        implements InitializingBean {

    @NotNull
    private MongoOperations mongoTemplate;

    @Document(collection="apioauth2scope")
    private class OpenScienceFrameworkScope {
        @Id
        private String id;
        private String name;
        private String description;
        private Boolean isActive;

        public String getName() {
            return this.name;
        }

        public String getDescription() {
            return this.description;
        }

        public Boolean getIsActive() {
            return this.isActive;
        }

        @Override
        public String toString() {
            return "OpenScienceFrameworkScope [id=" + this.id + ", name=" + this.name + "]";
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public Scope getScope(String name) {
        final OpenScienceFrameworkScope scope = this.mongoTemplate.findOne(new Query(
                new Criteria().andOperator(
                        Criteria.where("name").is(name),
                        Criteria.where("isActive").is(true)
                )
        ), OpenScienceFrameworkScope.class);

        if (scope == null) {
            return null;
        }

        return new Scope(scope.name, scope.description, false);
    }

    public void setMongoTemplate(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
