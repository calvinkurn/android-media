package com.tokopedia.autocompletecomponent.initialstate.domain.deleterecentsearch

import com.tokopedia.autocompletecomponent.initialstate.DELETE_RECENT_SEARCH_USE_CASE
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateScope
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class DeleteRecentSearchUseCaseTestModule {

//    @InitialStateScope
//    @Provides
//    @Named(DELETE_RECENT_SEARCH_USE_CASE)
//    fun provideDeleteRecentSearchUseCase(): UseCase<Boolean> =
//        DeleteRecentSearchUseCaseTestQuery(GraphqlUseCase())

    @InitialStateScope
    @Provides
    @Named(DELETE_RECENT_SEARCH_USE_CASE)
    fun provideDeleteRecentSearchUseCase(): UseCase<Boolean> =
        DeleteRecentSearchUseCaseTest()
}