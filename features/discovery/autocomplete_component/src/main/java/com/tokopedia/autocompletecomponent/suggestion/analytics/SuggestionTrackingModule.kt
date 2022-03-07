package com.tokopedia.autocompletecomponent.suggestion.analytics

import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionIrisAnalyticsModule
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionScope
import com.tokopedia.iris.Iris
import dagger.Module
import dagger.Provides

@Module(includes = [
    SuggestionIrisAnalyticsModule::class,
])
class SuggestionTrackingModule {

    @Provides
    @SuggestionScope
    fun provideSuggestionTracking(iris: Iris): SuggestionTracking {
        return SuggestionTracking(iris)
    }
}