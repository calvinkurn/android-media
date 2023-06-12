package com.tokopedia.autocompletecomponent.di

import com.tokopedia.autocompletecomponent.util.ProductionSchedulersProvider
import com.tokopedia.autocompletecomponent.util.SchedulersProvider
import dagger.Module
import dagger.Provides

@Module
object SchedulersProviderModule {
    @JvmStatic
    @Provides
    fun provideSchedulersProvider() : SchedulersProvider = ProductionSchedulersProvider
}
