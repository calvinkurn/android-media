package com.tokopedia.search.result.data.repository.dynamicfilter

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.coroutines.Repository
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.data.source.dynamicfilter.DynamicFilterCoroutineDataSource
import com.tokopedia.search.result.data.source.dynamicfilter.DynamicFilterCoroutineDataSourceModule
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@SearchScope
@Module(includes = [
    DynamicFilterCoroutineDataSourceModule::class
])
class DynamicFilterCoroutineRepositoryModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.DynamicFilter.DYNAMIC_FILTER_REPOSITORY)
    fun provideDynamicFilterRepository(dataSource: DynamicFilterCoroutineDataSource)
            : Repository<Map<String, Any>, DynamicFilterModel> {
        return DynamicFilterCoroutineRepository(dataSource, Dispatchers.IO)
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.DynamicFilter.DYNAMIC_FILTER_REPOSITORY_V4)
    fun provideDynamicFilterRepositoryV4(dataSource: DynamicFilterCoroutineDataSource)
            : Repository<Map<String, Any>, DynamicFilterModel> {
        return DynamicFilterCoroutineRepositoryV4(dataSource, Dispatchers.IO)
    }
}
