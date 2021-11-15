package com.tokopedia.autocompletecomponent.initialstate.domain.getinitialstate

import com.tokopedia.autocompletecomponent.initialstate.INITIAL_STATE_USE_CASE
import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateScope
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class InitialStateUseCaseTestModule {

//    @InitialStateScope
//    @Provides
//    @Named(INITIAL_STATE_USE_CASE)
//    fun provideUseCase(): UseCase<InitialStateUniverse> =
//        InitialStateUseCaseTestQuery(GraphqlUseCase())

    @InitialStateScope
    @Provides
    @Named(INITIAL_STATE_USE_CASE)
    fun provideUseCase(): UseCase<InitialStateUniverse> = InitialStateUseCaseTest()
}