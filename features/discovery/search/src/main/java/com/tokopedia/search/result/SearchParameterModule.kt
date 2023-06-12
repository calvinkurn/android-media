package com.tokopedia.search.result

import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class SearchParameterModule(
    private val searchParameter: SearchParameter
) {

    @SearchScope
    @Provides
    fun providesSearchParameter(): SearchParameter = searchParameter
}
