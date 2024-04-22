package com.tokopedia.autocompletecomponent.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics
import dagger.Module
import dagger.Provides

@Module
class IrisModule {
    @AutoCompleteScope
    @Provides
    fun provideIrisAnalytics(@ApplicationContext context: Context): Iris =
        IrisAnalytics.getInstance(context)
}
