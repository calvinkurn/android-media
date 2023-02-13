package com.tokopedia.content.common.producttag.di.module

import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateIrisAnalyticsModule
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateScope
import com.tokopedia.content.common.producttag.analytic.autocomplete.FeedAutoCompleteAnalytic
import com.tokopedia.content.common.producttag.analytic.autocomplete.FeedInitialStateTracking
import com.tokopedia.iris.Iris
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on May 24, 2022
 */
@Module(includes = [
    InitialStateIrisAnalyticsModule::class,
    FeedAutoCompleteAnalyticModule::class,
])
class FeedInitialStateTrackingModule {

    @Provides
    @InitialStateScope
    fun provideFeedInitialStateTracking(iris: Iris, analytic: FeedAutoCompleteAnalytic): InitialStateTracking {
        return FeedInitialStateTracking(iris, analytic)
    }
}