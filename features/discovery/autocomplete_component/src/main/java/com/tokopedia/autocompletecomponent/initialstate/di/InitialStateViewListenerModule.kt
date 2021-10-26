package com.tokopedia.autocompletecomponent.initialstate.di

import com.tokopedia.autocompletecomponent.initialstate.InitialStateFragment.InitialStateViewUpdateListener
import dagger.Module
import dagger.Provides

@Module
class InitialStateViewListenerModule(
    private val listener: InitialStateViewUpdateListener
) {

    @InitialStateScope
    @Provides
    fun provide(): InitialStateViewUpdateListener = listener
}