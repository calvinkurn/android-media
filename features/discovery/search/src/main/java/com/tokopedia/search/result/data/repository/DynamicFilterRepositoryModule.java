package com.tokopedia.search.result.data.repository;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.data.source.DynamicFilterDataSource;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class DynamicFilterRepositoryModule {

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
