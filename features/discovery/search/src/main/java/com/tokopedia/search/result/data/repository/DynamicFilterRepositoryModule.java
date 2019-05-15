package com.tokopedia.search.result.data.repository;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.data.gql.dynamicfilter.GqlDynamicFilterSpecModule;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module(includes = GqlDynamicFilterSpecModule.class)
public class DynamicFilterRepositoryModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.DynamicFilter.DYNAMIC_FILTER_GQL_REPOSITORY)
    Repository<DynamicFilterModel> provideDynamicFilterModelGqlRepository(
            @Named(SearchConstant.GQL.GQL_DYNAMIC_FILTER_SPEC) GqlSpecification specification) {
        return new DynamicFilterGqlRepository(specification);
    }
}
