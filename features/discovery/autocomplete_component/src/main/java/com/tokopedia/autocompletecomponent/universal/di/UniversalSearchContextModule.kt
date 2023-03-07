package com.tokopedia.autocompletecomponent.universal.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class UniversalSearchContextModule(private val context: Context) {

    @UniversalSearchContext
    @Provides
    fun provideUniversalSearchContext(): Context = context
}