package com.tokopedia.autocomplete.initialstate.di

import android.content.Context
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics
import dagger.Module
import dagger.Provides

@Module
class InitialStateIrisAnalyticsModule {
    @Provides
    @InitialStateScope
    fun provideIrisAnalytics(@InitialStateContext context: Context): Iris {
        return IrisAnalytics.getInstance(context)
    }
}