package com.tokopedia.search.result.mps.analytics

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class MPSTrackingModule {

    @Binds
    @SearchScope
    abstract fun bindMPSTracking(tracking: MPSTrackingDelegate): MPSTracking
}
