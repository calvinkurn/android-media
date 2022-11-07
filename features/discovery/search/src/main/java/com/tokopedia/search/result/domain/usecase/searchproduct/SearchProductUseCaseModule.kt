package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.config.GlobalConfig
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.data.mapper.searchproduct.SearchProductMapperModule
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.product.performancemonitoring.PerformanceMonitoringProvider
import com.tokopedia.search.utils.SearchLogger
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsModel
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
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
    fun provideSearchFirstPageUseCase(
        searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>,
        userSession: UserSessionInterface,
        coroutineDispatchers: CoroutineDispatchers,
        topAdsIrisSession: TopAdsIrisSession,
        performanceMonitoringProvider: PerformanceMonitoringProvider,
        remoteConfig: RemoteConfig,
    ): UseCase<SearchProductModel> {
        val firstPageGqlUseCase = provideSearchProductFirstPageUseCase(
            searchProductModelMapper,
            userSession,
            coroutineDispatchers,
            topAdsIrisSession,
            remoteConfig,
        )

        val topAdsGqlUseCase = provideSearchProductTopAdsUseCase()

        return SearchProductTypoCorrectionUseCase(
            firstPageGqlUseCase,
            topAdsGqlUseCase,
            performanceMonitoringProvider,
        )
    }

    private fun provideSearchProductFirstPageUseCase(
        searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>,
        userSession: UserSessionInterface,
        coroutineDispatchers: CoroutineDispatchers,
        topAdsIrisSession: TopAdsIrisSession,
        remoteConfig: RemoteConfig,
    ): UseCase<SearchProductModel> {
        val topAdsImageViewUseCase = TopAdsImageViewUseCase(
            userSession.userId,
            TopAdsRepository(),
            topAdsIrisSession.getSessionId()
        )
        return SearchProductFirstPageGqlUseCase(
            GraphqlUseCase(),
            searchProductModelMapper,
            topAdsImageViewUseCase,
            coroutineDispatchers,
            SearchLogger(
                remoteConfig,
                GlobalConfig.VERSION_CODE,
            ),
        )
    }

    private fun provideSearchProductTopAdsUseCase(): UseCase<TopAdsModel> {
        return SearchProductTopAdsUseCase(GraphqlUseCase())
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_USE_CASE)
    fun provideSearchLoadMoreUseCase(
        searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>,
        performanceMonitoringProvider: PerformanceMonitoringProvider,
    ): UseCase<SearchProductModel> {
        val loadMoreGqlUseCase = provideSearchProductLoadMoreUseCase(searchProductModelMapper)
        val topAdsGqlUseCase = provideSearchProductTopAdsUseCase()
        return SearchProductTypoCorrectionUseCase(
            loadMoreGqlUseCase,
            topAdsGqlUseCase,
            performanceMonitoringProvider,
        )
    }

    fun provideSearchProductLoadMoreUseCase(
        searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>
    ): UseCase<SearchProductModel> {
        return SearchProductLoadMoreGqlUseCase(GraphqlUseCase(), searchProductModelMapper)
    }
}
