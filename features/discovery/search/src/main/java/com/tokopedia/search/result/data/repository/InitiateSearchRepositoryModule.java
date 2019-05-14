package com.tokopedia.search.result.data.repository;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;

import javax.inject.Named;

import dagger.Provides;

public class InitiateSearchRepositoryModule {

    @SearchScope
    @Provides
    Repository<InitiateSearchModel> provideInitiateSearchModelRepository(
            @Named(SearchConstant.GQL.GQL_INITIATE_SEARCH) GqlSpecification specification) {
        return new InitiateSearchGqlRepository(specification);
    }
}
