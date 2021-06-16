package com.tokopedia.tokomart.search.di

import com.tokopedia.tokomart.search.utils.SEARCH_QUERY_PARAM_MAP
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SearchQueryParamModule(private val queryParamMap: Map<String, String>) {

    @Provides
    @SearchScope
    @Named(SEARCH_QUERY_PARAM_MAP)
    fun provideQueryParamMap() = queryParamMap
}