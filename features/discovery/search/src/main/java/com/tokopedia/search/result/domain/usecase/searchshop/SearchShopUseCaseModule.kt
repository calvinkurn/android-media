package com.tokopedia.search.result.domain.usecase.searchshop

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.R
import com.tokopedia.search.result.data.mapper.searchshop.SearchShopMapperModule
import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import rx.functions.Func1
import javax.inject.Named

@SearchScope
@Module(includes = [SearchShopMapperModule::class])
class SearchShopUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_FIRST_PAGE_USE_CASE)
    fun provideSearchShopFirstPageUseCase(
            @ApplicationContext context: Context,
            searchShopModelMapper: Func1<GraphqlResponse, SearchShopModel>
    ): UseCase<SearchShopModel> {
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.gql_search_shop_first_page),
                SearchShopModel::class.java
        )

        return SearchShopFirstPageGqlUseCase(graphqlRequest, GraphqlUseCase(), searchShopModelMapper)
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_LOAD_MORE_USE_CASE)
    fun provideSearchShopLoadMoreUseCase(
            @ApplicationContext context: Context,
            searchShopModelMapper: Func1<GraphqlResponse, SearchShopModel>
    ): UseCase<SearchShopModel> {
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.gql_search_shop_load_more),
                SearchShopModel::class.java
        )

        return SearchShopLoadMoreGqlUseCase(graphqlRequest, GraphqlUseCase(), searchShopModelMapper)
    }
}
