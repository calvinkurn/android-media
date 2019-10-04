package com.tokopedia.search.result.shop.repository

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.coroutines.Repository
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.remoteconfig.GraphqlHelper
import com.tokopedia.search.R
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SearchScope
@Module
class SearchShopRepositoryModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_FIRST_PAGE_REPOSITORY)
    fun provideSearchShopFirstPageRepository(
            @ApplicationContext context: Context
    ): Repository<Map<String, Any>, SearchShopModel> {

        return SearchShopFirstPageRepository(
                GraphqlHelper.loadRawString(context.resources, R.raw.gql_search_shop_first_page),
                GraphqlCacheStrategy.Builder(CacheType.NONE).build(),
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_LOAD_MORE_REPOSITORY)
    fun provideSearchShopLoadMoreRepository(
            @ApplicationContext context: Context
    ): Repository<Map<String, Any>, SearchShopModel> {

        return SearchShopLoadMoreRepository(
                GraphqlHelper.loadRawString(context.resources, R.raw.gql_search_shop_load_more),
                GraphqlCacheStrategy.Builder(CacheType.NONE).build(),
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }
}