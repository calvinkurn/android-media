package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.config.GlobalConfig
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.reimagine.ReimagineRollence
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
        reimagineRollence: ReimagineRollence,
    ): UseCase<SearchProductModel> {
        val firstPageGqlUseCase = provideSearchProductFirstPageUseCase(
            searchProductModelMapper,
            userSession,
            coroutineDispatchers,
            topAdsIrisSession,
            remoteConfig,
            reimagineRollence,
        )

        val topAdsGqlUseCase = provideSearchProductTopAdsUseCase()

        return SearchProductTypoCorrectionUseCase(
            searchProductUseCase = firstPageGqlUseCase,
            searchProductTopAdsUseCase = topAdsGqlUseCase,
            performanceMonitoringProvider = performanceMonitoringProvider,
            reimagineRollence = reimagineRollence,
        )
    }

    private fun provideSearchProductFirstPageUseCase(
        searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>,
        userSession: UserSessionInterface,
        coroutineDispatchers: CoroutineDispatchers,
        topAdsIrisSession: TopAdsIrisSession,
        remoteConfig: RemoteConfig,
        reimagineRollence: ReimagineRollence,
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
                userSession.userId
            ),
            reimagineRollence,
        )
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_USE_CASE)
    fun provideSearchLoadMoreUseCase(
        searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>,
        performanceMonitoringProvider: PerformanceMonitoringProvider,
        reimagineRollence: ReimagineRollence,
    ): UseCase<SearchProductModel> {
        val loadMoreGqlUseCase = provideSearchProductLoadMoreUseCase(
            searchProductModelMapper,
            reimagineRollence,
        )
        val topAdsGqlUseCase = provideSearchProductTopAdsUseCase()
        return SearchProductTypoCorrectionUseCase(
            searchProductUseCase = loadMoreGqlUseCase,
            searchProductTopAdsUseCase = topAdsGqlUseCase,
            performanceMonitoringProvider = performanceMonitoringProvider,
            reimagineRollence = reimagineRollence,
        )
    }

    private fun provideSearchProductLoadMoreUseCase(
        searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>,
        reimagineRollence: ReimagineRollence,
    ): UseCase<SearchProductModel> {
        return SearchProductLoadMoreGqlUseCase(
            GraphqlUseCase(),
            searchProductModelMapper,
            reimagineRollence,
        )
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_TOPADS_USE_CASE)
    fun provideSearchProductTopAdsUseCase(): UseCase<TopAdsModel> {
        return SearchProductTopAdsUseCase(GraphqlUseCase())
    }
}
