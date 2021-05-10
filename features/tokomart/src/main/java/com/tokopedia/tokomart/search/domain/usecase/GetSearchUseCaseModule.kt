package com.tokopedia.tokomart.search.domain.usecase

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.tokomart.search.di.SearchScope
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.search.utils.SEARCH_FIRST_PAGE_USE_CASE
import com.tokopedia.tokomart.search.utils.SEARCH_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GetSearchUseCaseModule {

    @Provides
    @SearchScope
    fun provideMultiRequestGraphqlUseCase() = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @SearchScope
    @Provides
    @Named(SEARCH_FIRST_PAGE_USE_CASE)
    fun provideSearchFirstPageUseCase(
            graphqlUseCase: MultiRequestGraphqlUseCase,
    ): UseCase<SearchModel> {
        return GetSearchFirstPageUseCase(graphqlUseCase)
    }

    @SearchScope
    @Provides
    @Named(SEARCH_LOAD_MORE_PAGE_USE_CASE)
    fun provideSearchLoadMorePageUseCase(): UseCase<SearchModel> {
        return GetSearchLoadMorePageUseCase()
    }
}