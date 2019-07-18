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
import com.tokopedia.search.result.domain.model.SearchShopModelKt
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
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_USE_CASE)
    internal fun provideSearchShopModel(
            @ApplicationContext context: Context,
            searchShopModelMapper: Func1<GraphqlResponse, SearchShopModelKt>
    ): UseCase<SearchShopModelKt> {
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.gql_search_shop),
                SearchShopModelKt::class.java
        )

        return SearchShopGqlUseCase(graphqlRequest, GraphqlUseCase(), searchShopModelMapper)
    }
}
