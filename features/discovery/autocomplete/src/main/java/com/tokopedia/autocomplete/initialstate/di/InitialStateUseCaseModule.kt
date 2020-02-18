package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.di.AutoCompleteScope
import com.tokopedia.autocomplete.initialstate.InitialStateRepository
import com.tokopedia.autocomplete.initialstate.InitialStateUseCase
import dagger.Module
import dagger.Provides

@AutoCompleteScope
@Module
class InitialStateUseCaseModule {

    @AutoCompleteScope
    @Provides
    internal fun provideInitialStateUseCase(
            initialStateRepository: InitialStateRepository
    ): InitialStateUseCase {
        return InitialStateUseCase(
                initialStateRepository
        )
    }
}