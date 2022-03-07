package com.tokopedia.autocompletecomponent.initialstate.domain.refreshinitialstate

import com.tokopedia.autocompletecomponent.initialstate.REFRESH_INITIAL_STATE_USE_CASE
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateScope
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class RefreshInitialStateUseCaseTestModule {

    @Provides
    @InitialStateScope
    @Named(REFRESH_INITIAL_STATE_USE_CASE)
    fun provideRefreshInitialState(): UseCase<List<InitialStateData>> =
        RefreshInitialStateUseCaseTest()
}