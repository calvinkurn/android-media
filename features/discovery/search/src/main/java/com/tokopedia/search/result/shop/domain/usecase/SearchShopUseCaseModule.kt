package com.tokopedia.search.result.shop.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.search.R
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.common.UseCase
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SearchScope
@Module
internal class SearchShopUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_FIRST_PAGE_USE_CASE)
    fun provideSearchShopFirstPageUseCase(@ApplicationContext context: Context): UseCase<SearchShopModel> {
        return SearchShopFirstPageUseCase(
                GraphqlHelper.loadRawString(context.resources, R.raw.gql_search_shop_first_page),
                GraphqlCacheStrategy.Builder(CacheType.NONE).build(),
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_LOAD_MORE_USE_CASE)
    fun provideSearchShopLoadMoreUseCase(@ApplicationContext context: Context): UseCase<SearchShopModel> {
        return SearchShopLoadMoreUseCase(
                GraphqlHelper.loadRawString(context.resources, R.raw.gql_search_shop_load_more),
                GraphqlCacheStrategy.Builder(CacheType.NONE).build(),
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }
}