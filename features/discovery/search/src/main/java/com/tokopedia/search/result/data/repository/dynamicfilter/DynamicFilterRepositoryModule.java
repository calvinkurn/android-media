package com.tokopedia.search.result.data.repository.dynamicfilter;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.common.repository.gql.GqlRepository;
import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.data.gql.dynamicfilter.GqlDynamicFilterResponse;
import com.tokopedia.search.result.data.gql.dynamicfilter.GqlDynamicFilterSpecModule;
import com.tokopedia.search.result.data.source.DynamicFilterDataSource;
import com.tokopedia.search.result.data.source.DynamicFilterDataSourceModule;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module(includes = {
        GqlDynamicFilterSpecModule.class,
        DynamicFilterDataSourceModule.class
})
public class DynamicFilterRepositoryModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.DynamicFilter.DYNAMIC_FILTER_GQL_REPOSITORY)
    Repository<DynamicFilterModel> provideDynamicFilterModelGqlRepository(
            @Named(SearchConstant.GQL.GQL_DYNAMIC_FILTER_RESPONSE_REPOSITORY) GqlRepository<GqlDynamicFilterResponse> gqlDynamicFilterResponseRepository
    ) {
        return new DynamicFilterGqlRepository(gqlDynamicFilterResponseRepository);
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.GQL.GQL_DYNAMIC_FILTER_RESPONSE_REPOSITORY)
    GqlRepository<GqlDynamicFilterResponse> provideGqlDynamicFilterReponseRepository(
            @Named(SearchConstant.GQL.GQL_DYNAMIC_FILTER_SPEC) GqlSpecification specification) {
        return new GqlRepository<>(specification);
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.DynamicFilter.DYNAMIC_FILTER_REPOSITORY)
    Repository<DynamicFilterModel> provideDynamicFilterRepository(DynamicFilterDataSource dynamicFilterDataSource) {
        return new DynamicFilterRepository(dynamicFilterDataSource);
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.DynamicFilter.DYNAMIC_FILTER_REPOSITORY_V4)
    Repository<DynamicFilterModel> provideDynamicFilterRepositoryV4(DynamicFilterDataSource dynamicFilterDataSource) {
        return new DynamicFilterRepositoryV4(dynamicFilterDataSource);
    }
}
