package com.tokopedia.autocomplete.suggestion.di

import android.content.Context
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics
import dagger.Module
import dagger.Provides

@Module
class SuggestionIrisAnalyticsModule {
    @Provides
    @SuggestionScope
    fun provideIrisAnalytics(@SuggestionContext context: Context): Iris {
        return IrisAnalytics.getInstance(context)
    }
}