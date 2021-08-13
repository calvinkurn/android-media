package com.tokopedia.autocomplete.initialstate.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class InitialStateContextModule(private val context: Context) {

    @InitialStateContext
    @Provides
    fun provideInitialStateContext(): Context = context
}