package com.tokopedia.tokopedianow.search.domain.usecase

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.tokopedianow.search.di.SearchScope
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.search.utils.SEARCH_FIRST_PAGE_USE_CASE
import com.tokopedia.tokopedianow.search.utils.SEARCH_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GetSearchUseCaseModule {

    @SearchScope
    @Provides
    @Named(SEARCH_FIRST_PAGE_USE_CASE)
    fun provideSearchFirstPageUseCase(): UseCase<SearchModel> {
        return GetSearchFirstPageUseCase(GraphqlInteractor.getInstance().multiRequestGraphqlUseCase)
    }

    @SearchScope
    @Provides
    @Named(SEARCH_LOAD_MORE_PAGE_USE_CASE)
    fun provideSearchLoadMorePageUseCase(): UseCase<SearchModel> {
        return GetSearchLoadMorePageUseCase(GraphqlInteractor.getInstance().multiRequestGraphqlUseCase)
    }
}