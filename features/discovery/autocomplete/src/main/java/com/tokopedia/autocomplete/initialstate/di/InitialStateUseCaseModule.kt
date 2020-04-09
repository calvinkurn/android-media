package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.initialstate.InitialStateRepository
import com.tokopedia.autocomplete.initialstate.InitialStateUseCase
import dagger.Module
import dagger.Provides

@InitialStateScope
@Module
class InitialStateUseCaseModule {

    @InitialStateScope
    @Provides
    internal fun provideInitialStateUseCase(
            initialStateRepository: InitialStateRepository
    ): InitialStateUseCase {
        return InitialStateUseCase(
                initialStateRepository
        )
    }
}