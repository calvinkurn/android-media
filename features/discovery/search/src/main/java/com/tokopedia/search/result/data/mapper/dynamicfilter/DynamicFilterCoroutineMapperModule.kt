package com.tokopedia.search.result.data.mapper.dynamicfilter

import com.google.gson.Gson
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides
import retrofit2.Response

@SearchScope
@Module
class DynamicFilterCoroutineMapperModule {

    @SearchScope
    @Provides
    fun provideDynamicFilterMapper(gson: Gson): Mapper<Response<String>, DynamicFilterModel> {
        return DynamicFilterCoroutineMapper(gson)
    }
}
