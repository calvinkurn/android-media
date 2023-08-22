package com.tokopedia.autocompletecomponent.initialstate.di

import com.tokopedia.autocompletecomponent.initialstate.InitialStateContract
import com.tokopedia.autocompletecomponent.initialstate.InitialStatePresenter
import dagger.Module
import dagger.Provides

@Module(includes = [
    InitialStateUserSessionInterfaceModule::class,
    InitialStateContextModule::class,
])
class InitialStatePresenterModule {

    @InitialStateScope
    @Provides
    fun provideInitialStatePresenter(
        initialStatePresenter: InitialStatePresenter
    ): InitialStateContract.Presenter = initialStatePresenter
}
