package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.initialstate.InitialStateData
import com.tokopedia.autocomplete.initialstate.InitialStateRepository
import com.tokopedia.autocomplete.initialstate.REFRESH_INITIAL_STATE_USE_CASE
import com.tokopedia.autocomplete.initialstate.popularsearch.RefreshInitialStateUseCase
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class RefreshInitialStateUseCaseModule {

    @InitialStateScope
    @Provides
    @Named(REFRESH_INITIAL_STATE_USE_CASE)
    internal fun provideRefreshInitialStateUseCase(
            initialStateRepository: InitialStateRepository
    ): UseCase<List<InitialStateData>> {
        return RefreshInitialStateUseCase(
                initialStateRepository
        )
    }
}