package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.initialstate.recentsearch.DeleteRecentSearchUseCase
import com.tokopedia.autocomplete.initialstate.InitialStateRepository
import com.tokopedia.autocomplete.initialstate.InitialStateUseCase
import dagger.Module
import dagger.Provides

@InitialStateScope
@Module
class DeleteRecentSearchUseCaseModule {

    @InitialStateScope
    @Provides
    internal fun provideDeleteRecentSearchUseCase(
            initialStateRepository: InitialStateRepository,
            initialStateUseCase: InitialStateUseCase
    ): DeleteRecentSearchUseCase {
        return DeleteRecentSearchUseCase(
                initialStateRepository,
                initialStateUseCase
        )
    }
}