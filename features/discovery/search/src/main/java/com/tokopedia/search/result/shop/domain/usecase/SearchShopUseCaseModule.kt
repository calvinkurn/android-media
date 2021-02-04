package com.tokopedia.search.result.shop.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class SearchShopUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_FIRST_PAGE_USE_CASE)
    fun provideSearchShopFirstPageUseCase(): UseCase<SearchShopModel> {
        return SearchShopFirstPageUseCase(
                GraphqlCacheStrategy.Builder(CacheType.NONE).build(),
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_LOAD_MORE_USE_CASE)
    fun provideSearchShopLoadMoreUseCase(): UseCase<SearchShopModel> {
        return SearchShopLoadMoreUseCase(
                GraphqlCacheStrategy.Builder(CacheType.NONE).build(),
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.GET_SHOP_COUNT_USE_CASE)
    fun provideGetShopCountUseCase(): UseCase<Int> {
        return GetShopCountUseCase(GraphqlUseCase(GraphqlInteractor.getInstance().graphqlRepository))
    }
}