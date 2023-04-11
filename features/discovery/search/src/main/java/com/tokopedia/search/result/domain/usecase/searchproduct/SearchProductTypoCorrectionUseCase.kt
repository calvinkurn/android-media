package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_PARAMS
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.product.performancemonitoring.PerformanceMonitoringProvider
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_NETWORK_USE_CASE_ATTRIBUTION
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_NETWORK_USE_CASE_NORMAL
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_NETWORK_USE_CASE_TYPO_CORRECTED
import com.tokopedia.search.utils.ProductionSchedulersProvider
import com.tokopedia.search.utils.SchedulersProvider
import com.tokopedia.topads.sdk.domain.model.TopAdsModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class SearchProductTypoCorrectionUseCase(
    private val searchProductUseCase: UseCase<SearchProductModel>,
    private val searchProductTopAdsUseCase: UseCase<TopAdsModel>,
    performanceMonitoringProvider: PerformanceMonitoringProvider,
    schedulersProvider: SchedulersProvider = ProductionSchedulersProvider(),
) : UseCase<SearchProductModel>(
    schedulersProvider.io(),
    schedulersProvider.ui()
) {
    private val typoCorrectionCodes = listOf("3", "6")
    private val performanceMonitoring: PageLoadTimePerformanceInterface? =
        performanceMonitoringProvider.get()

    private fun shouldCallTopAdsGqlForTypoCorrection(
        searchProductModel: SearchProductModel
    ): Boolean {
        return searchProductModel.searchProduct.header.responseCode in typoCorrectionCodes
    }

    override fun createObservable(
        requestParams: RequestParams
    ): Observable<SearchProductModel> {
        return searchProductUseCase.createObservable(requestParams)
            .switchMap { searchProductModel ->
                if (shouldCallTopAdsGqlForTypoCorrection(searchProductModel)) {
                    performanceMonitoring?.addAttribution(
                        SEARCH_RESULT_PLT_NETWORK_USE_CASE_ATTRIBUTION,
                        SEARCH_RESULT_PLT_NETWORK_USE_CASE_TYPO_CORRECTED
                    )
                    callTopAdsUseCase(requestParams, searchProductModel)
                } else {
                    performanceMonitoring?.addAttribution(
                        SEARCH_RESULT_PLT_NETWORK_USE_CASE_ATTRIBUTION,
                        SEARCH_RESULT_PLT_NETWORK_USE_CASE_NORMAL
                    )
                    Observable.just(searchProductModel)
                }
            }
    }

    private fun callTopAdsUseCase(
        requestParams: RequestParams,
        searchProductModel: SearchProductModel,
    ): Observable<SearchProductModel> {
        val modifiedSearchParameter = mutableMapOf<String, Any>().apply {
            putAll(requestParams.parameters[SEARCH_PRODUCT_PARAMS] as Map<String, Any>)
            put(SearchApiConst.Q, searchProductModel.searchProduct.data.related.relatedKeyword)
            put(SearchApiConst.IS_TYPO_CORRECTED, true)
        }
        val topAdsRequestParams = RequestParams.create().apply {
            putAll(modifiedSearchParameter)
        }
        return searchProductTopAdsUseCase.createObservable(topAdsRequestParams)
            .map { topAdsModel ->
                searchProductModel.copy(topAdsModel = topAdsModel)
            }
            .onErrorReturn {
                searchProductModel.copy(topAdsModel = TopAdsModel())
            }
    }
}
