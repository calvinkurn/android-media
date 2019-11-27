package com.tokopedia.search.result.data.source.dynamicfilter

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.di.qualifier.AceQualifier
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.data.mapper.dynamicfilter.DynamicFilterCoroutineMapperModule
import com.tokopedia.search.result.network.service.BrowseApi
import com.tokopedia.search.result.network.service.BrowseApiModule
import dagger.Module
import dagger.Provides
import retrofit2.Response

@SearchScope
@Module(includes = [
    BrowseApiModule::class,
    DynamicFilterCoroutineMapperModule::class
])
class DynamicFilterCoroutineDataSourceModule {

    @SearchScope
    @Provides
    fun provideDynamicFilterDataSource(
            @AceQualifier browseApi: BrowseApi,
            mapper: Mapper<Response<String>, DynamicFilterModel>
    ): DynamicFilterCoroutineDataSource {
        return DynamicFilterCoroutineDataSource(browseApi, mapper)
    }
}
