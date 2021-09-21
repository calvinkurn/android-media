package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.data.mapper.searchproduct.SearchProductMapperModule
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.utils.SearchLogger
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import rx.functions.Func1
import javax.inject.Named

@Module(includes = [SearchProductMapperModule::class])
class SearchProductUseCaseModule {
    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_FIRST_PAGE_USE_CASE)
    fun provideSearchProductFirstPageUseCase(
            searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>,
            userSession: UserSessionInterface,
            coroutineDispatchers: CoroutineDispatchers
    ): UseCase<SearchProductModel> {
        val topAdsImageViewUseCase = TopAdsImageViewUseCase(
                userSession.userId,
                TopAdsRepository()
        )
        return SearchProductFirstPageGqlUseCase(
                GraphqlUseCase(),
                searchProductModelMapper,
                topAdsImageViewUseCase,
                coroutineDispatchers,
                SearchLogger()
        )
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_USE_CASE)
    fun provideSearchProductLoadMoreUseCase(
            searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>
    ): UseCase<SearchProductModel> {
        return SearchProductLoadMoreGqlUseCase(GraphqlUseCase(), searchProductModelMapper)
    }
}