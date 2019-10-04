package com.tokopedia.search.result.shop.domain.usecase

import android.content.Context
import android.support.annotation.RawRes
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.search.R
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SearchScope
@Module
class SearchShopCoroutineUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_FIRST_PAGE_REPOSITORY)
    fun provideSearchShopFirstPageUseCase(
            @ApplicationContext context: Context
    ): SearchUseCase<SearchShopModel> {
        val graphqlUseCase = createGraphqlUseCase(context, R.raw.gql_search_shop_first_page)

        return SearchShopFirstPageUseCase(graphqlUseCase)
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_LOAD_MORE_REPOSITORY)
    fun provideSearchShopLoadMoreUseCase(
            @ApplicationContext context: Context
    ): SearchUseCase<SearchShopModel> {
        val graphqlUseCase = createGraphqlUseCase(context, R.raw.gql_search_shop_load_more)

        return SearchShopLoadMoreUseCase(graphqlUseCase)
    }

    private fun createGraphqlUseCase(context: Context, @RawRes rawQueryRes: Int): GraphqlUseCase<SearchShopModel> {
        val query = GraphqlHelper.loadRawString(context.resources, rawQueryRes)
        val repository = GraphqlInteractor.getInstance().graphqlRepository

        val useCase = GraphqlUseCase<SearchShopModel>(repository)
        useCase.setTypeClass(SearchShopModel::class.java)
        useCase.setGraphqlQuery(query)

        return useCase
    }
}