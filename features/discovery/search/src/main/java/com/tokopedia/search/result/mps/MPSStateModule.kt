package com.tokopedia.search.result.mps

import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class MPSStateModule(
    private val mpsState: MPSState = MPSState()
) {

    @Provides
    @SearchScope
    fun provideMPSState() = mpsState
}
