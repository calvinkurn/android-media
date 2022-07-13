package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_PARAMS
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.utils.ProductionSchedulersProvider
import com.tokopedia.search.utils.SchedulersProvider
import com.tokopedia.topads.sdk.domain.model.TopAdsModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class SearchProductTypoCorrectionUseCase(
    private val searchProductUseCase: UseCase<SearchProductModel>,
    private val searchProductTopAdsUseCase: UseCase<TopAdsModel>,
    private val remoteConfig: RemoteConfig,
    schedulersProvider: SchedulersProvider = ProductionSchedulersProvider()
) : UseCase<SearchProductModel>(
    schedulersProvider.io(),
    schedulersProvider.ui()
) {
    private val typoCorrectionCodes = listOf("3", "6")

    private val isABTestTypoCorrectionAds: Boolean by lazy {
        getABTestTypoCorrectionAds()
    }

    private fun getABTestTypoCorrectionAds(): Boolean {
        return try {
            val abTestTypoCorrectionAds = remoteConfig.getString(
                RollenceKey.SEARCH_TYPO_CORRECTION_ADS,
                ""
            )
            RollenceKey.SEARCH_TYPO_CORRECTION_ADS_VARIANT == abTestTypoCorrectionAds
        } catch (e: Exception) {
            false
        }
    }

    private fun shouldCallTopAdsGqlForTypoCorrection(
        searchProductModel: SearchProductModel
    ): Boolean {
        return isABTestTypoCorrectionAds
                && searchProductModel.searchProduct.header.responseCode in typoCorrectionCodes
    }

    override fun createObservable(
        requestParams: RequestParams
    ): Observable<SearchProductModel> {
        return searchProductUseCase.createObservable(requestParams)
            .switchMap { searchProductModel ->
                if (shouldCallTopAdsGqlForTypoCorrection(searchProductModel)) {
                    callTopAdsUseCase(requestParams, searchProductModel)
                } else {
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