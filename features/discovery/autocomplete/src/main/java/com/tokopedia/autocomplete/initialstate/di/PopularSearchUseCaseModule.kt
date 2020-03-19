package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.initialstate.InitialStateRepository
import com.tokopedia.autocomplete.initialstate.popularsearch.RefreshPopularSearchUseCase
import dagger.Module
import dagger.Provides

@InitialStateScope
@Module
class PopularSearchUseCaseModule {

    @InitialStateScope
    @Provides
    internal fun providePopularSearchUseCase(
            initialStateRepository: InitialStateRepository
    ): RefreshPopularSearchUseCase {
        return RefreshPopularSearchUseCase(
                initialStateRepository
        )
    }
}