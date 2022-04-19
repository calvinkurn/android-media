package com.tokopedia.autocompletecomponent.initialstate.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics
import dagger.Module
import dagger.Provides

@Module
class InitialStateIrisAnalyticsModule {

    @InitialStateScope
    @Provides
    fun provideIrisAnalytics(@ApplicationContext context: Context): Iris {
        return IrisAnalytics.getInstance(context)
    }
}