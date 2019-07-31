package com.tokopedia.search.result.data.repository.dynamicfilter;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.data.source.dynamicfilter.DynamicFilterDataSource;
import com.tokopedia.search.result.data.source.dynamicfilter.DynamicFilterDataSourceModule;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module(includes = {
        DynamicFilterDataSourceModule.class
})
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
