package com.tokopedia.autocompletecomponent.initialstate.analytics

import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateIrisAnalyticsModule
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateScope
import com.tokopedia.iris.Iris
import dagger.Module
import dagger.Provides

@Module(includes = [InitialStateIrisAnalyticsModule::class])
class InitialStateTrackingModule {

    @Provides
    @InitialStateScope
    fun provideInitialStateTracking(iris: Iris): InitialStateTracking {
        return InitialStateTracking(iris)
    }
}