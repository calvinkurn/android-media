package com.tokopedia.content.product.picker.ugc.di.module

import com.tokopedia.autocompletecomponent.suggestion.analytics.SuggestionTracking
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionIrisAnalyticsModule
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionScope
import com.tokopedia.content.product.picker.ugc.analytic.autocomplete.FeedAutoCompleteAnalytic
import com.tokopedia.content.product.picker.ugc.analytic.autocomplete.FeedSuggestionTracking
import com.tokopedia.iris.Iris
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on May 24, 2022
 */
@Module(includes = [
    SuggestionIrisAnalyticsModule::class,
    FeedAutoCompleteAnalyticModule::class,
])
class FeedSuggestionTrackingModule {

    @Provides
    @SuggestionScope
    fun provideSuggestionTracking(iris: Iris, analytic: FeedAutoCompleteAnalytic): SuggestionTracking {
        return FeedSuggestionTracking(iris, analytic)
    }
}
