package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.di.AutoCompleteScope
import com.tokopedia.autocomplete.initialstate.InitialStateRepository
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchUseCase
import dagger.Module
import dagger.Provides

@AutoCompleteScope
@Module
class PopularSearchUseCaseModule {

    @AutoCompleteScope
    @Provides
    internal fun providePopularSearchUseCase(
            initialStateRepository: InitialStateRepository
    ): PopularSearchUseCase {
        return PopularSearchUseCase(
                initialStateRepository
        )
    }
}