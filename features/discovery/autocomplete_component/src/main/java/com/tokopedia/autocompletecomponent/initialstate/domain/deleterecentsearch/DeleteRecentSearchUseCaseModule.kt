package com.tokopedia.autocompletecomponent.initialstate.domain.deleterecentsearch

import com.tokopedia.autocompletecomponent.initialstate.DELETE_RECENT_SEARCH_USE_CASE
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateScope
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class DeleteRecentSearchUseCaseModule {

    @InitialStateScope
    @Provides
    @Named(DELETE_RECENT_SEARCH_USE_CASE)
    fun provideDeleteRecentSearchUseCase(): UseCase<Boolean> {
        return DeleteRecentSearchUseCase(GraphqlUseCase())
    }
}