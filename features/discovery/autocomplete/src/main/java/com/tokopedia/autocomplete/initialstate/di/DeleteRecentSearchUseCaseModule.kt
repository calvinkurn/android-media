package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.initialstate.recentsearch.DeleteRecentSearchUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides

@Module
class DeleteRecentSearchUseCaseModule {

    @InitialStateScope
    @Provides
    internal fun provideDeleteRecentSearchUseCase(): UseCase<Boolean> {
        return DeleteRecentSearchUseCase(GraphqlUseCase())
    }
}