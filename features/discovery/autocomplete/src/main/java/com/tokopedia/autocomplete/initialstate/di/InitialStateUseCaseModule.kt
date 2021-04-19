package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.initialstate.INITIAL_STATE_USE_CASE
import com.tokopedia.autocomplete.initialstate.InitialStateData
import com.tokopedia.autocomplete.initialstate.InitialStateUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class InitialStateUseCaseModule {

    @InitialStateScope
    @Provides
    @Named(INITIAL_STATE_USE_CASE)
    internal fun provideInitialStateUseCase(): UseCase<List<InitialStateData>> {
        return InitialStateUseCase(GraphqlUseCase())
    }
}