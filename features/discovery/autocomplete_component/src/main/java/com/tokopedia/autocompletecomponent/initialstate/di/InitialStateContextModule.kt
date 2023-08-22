package com.tokopedia.autocompletecomponent.initialstate.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class InitialStateContextModule(private val context: Context) {

    @InitialStateScope
    @Provides
    fun provideSearchContext(): Context = context
}
