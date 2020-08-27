package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.initialstate.InitialStateData
import com.tokopedia.autocomplete.initialstate.InitialStateRepository
import com.tokopedia.autocomplete.initialstate.NAMED_USE_CASE_REFRESH_INITIAL_STATE
import com.tokopedia.autocomplete.initialstate.popularsearch.RefreshInitialStateUseCase
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@InitialStateScope
@Module
class RefreshInitialStateUseCaseModule {

    @InitialStateScope
    @Provides
    @Named(NAMED_USE_CASE_REFRESH_INITIAL_STATE)
    internal fun provideRefreshInitialStateUseCase(
            initialStateRepository: InitialStateRepository
    ): UseCase<List<InitialStateData>> {
        return RefreshInitialStateUseCase(
                initialStateRepository
        )
    }
}