package com.tokopedia.search.result

import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class SearchStateModule {

    @SearchScope
    @Provides
    fun provideSearchState(
        searchParameter: SearchParameter,
    ): SearchState =
        SearchState(searchParameter.getSearchParameterHashMap())
}
