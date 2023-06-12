package com.tokopedia.search.result.mps

import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class MPSStateModule {

    @Provides
    @SearchScope
    fun provideMPSState(
        searchParameter: SearchParameter,
    ) = MPSState(
        parameter = searchParameter.getSearchParameterHashMap()
    )
}
