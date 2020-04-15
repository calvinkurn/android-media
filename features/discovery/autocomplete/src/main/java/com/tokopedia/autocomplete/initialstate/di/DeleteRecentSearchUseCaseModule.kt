package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.initialstate.recentsearch.DeleteRecentSearchUseCase
import com.tokopedia.autocomplete.initialstate.InitialStateRepository
import dagger.Module
import dagger.Provides

@InitialStateScope
@Module
class DeleteRecentSearchUseCaseModule {

    @InitialStateScope
    @Provides
    internal fun provideDeleteRecentSearchUseCase(
            initialStateRepository: InitialStateRepository
    ): DeleteRecentSearchUseCase {
        return DeleteRecentSearchUseCase(
                initialStateRepository
        )
    }
}