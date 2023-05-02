package com.tokopedia.search.result.product.ads

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressPresenterDelegate
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.topads.sdk.domain.model.TopAdsModel
import com.tokopedia.usecase.UseCase
import dagger.Lazy
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

@SearchScope
class AdsLowOrganic @Inject constructor(
    @param:Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_TOPADS_USE_CASE)
    private val topAdsUseCase: UseCase<TopAdsModel>,
    @param:Named(SearchConstant.AB_TEST_REMOTE_CONFIG)
    private val abTestRemoteConfig: Lazy<RemoteConfig>,
    private val viewUpdater: ViewUpdater,
    private val requestParamsGenerator: RequestParamsGenerator,
    private val chooseAddressDelegate: ChooseAddressPresenterDelegate,
) {

    val isEnabledRollence by lazy(LazyThreadSafetyMode.NONE, ::getIsEnabledRollence)

    private fun getIsEnabledRollence() = try {
        abTestRemoteConfig.get().getString(EXP_NAME, "") == VAR_ADS
    } catch (ignored: Throwable) {
        false
    }

    private var lastPosition = 0
    private var canLoadMore = true

    fun processAdsLowOrganic(
        isHideProductAds: Boolean,
        keyword: String,
        topAdsModel: TopAdsModel,
        productData: SearchPageProductData,
        action: (List<Visitable<*>>) -> Unit,
    ) {
        if (isHideProductAds || !isEnabledRollence || topAdsModel.data.isEmpty()) return

        val topAdsProductList = createTopAdsProductList(topAdsModel, productData)

        lastPosition = topAdsProductList.lastOrNull()?.position ?: 0

        val adsLowOrganicVisitableList = mutableListOf<Visitable<*>>()

        adsLowOrganicVisitableList.add(AdsLowOrganicTitleDataView.create(keyword))
        adsLowOrganicVisitableList.addAll(topAdsProductList)

        action(adsLowOrganicVisitableList)
    }

    private fun createTopAdsProductList(
        topAdsModel: TopAdsModel,
        productData: SearchPageProductData,
    ) = topAdsModel.data.mapIndexed { index, data ->
        ProductItemDataView.create(
            data,
            lastPosition + index + 1,
            productData.dimension90,
            productData.productListType,
            productData.externalReference,
            productData.keywordIntention,
            false,
        )
    }

    fun loadNextPage(
        searchParameter: Map<String, Any>,
        productData: SearchPageProductData,
        onSuccessListener: () -> Unit,
    ) {
        if (!canLoadNextPage()) return

        viewUpdater.addLoading()

        val requestParams = requestParamsGenerator.createInitializeSearchParam(
            searchParameter,
            chooseAddressDelegate.getChooseAddressParams(),
        )

        topAdsUseCase.execute(requestParams, topAdsNextPageSubscriber(productData, onSuccessListener))
    }

    private fun canLoadNextPage() = isEnabledRollence && canLoadMore

    private fun topAdsNextPageSubscriber(
        productData: SearchPageProductData,
        onSuccessListener: () -> Unit,
    ) = object : Subscriber<TopAdsModel>() {
        override fun onCompleted() { }

        override fun onError(e: Throwable?) {
            canLoadMore = false
            viewUpdater.removeLoading()
        }

        override fun onNext(topAdsModel: TopAdsModel) {
            onTopAdsNextPageSuccessful(
                topAdsModel,
                productData,
                onSuccessListener,
            )
        }
    }

    private fun onTopAdsNextPageSuccessful(
        topAdsModel: TopAdsModel,
        productData: SearchPageProductData,
        onSuccessListener: () -> Unit,
    ) {
        if (topAdsModel.data.isEmpty()) {
            canLoadMore = false
            viewUpdater.removeLoading()
            return
        }

        val topAdsProductList = createTopAdsProductList(topAdsModel, productData)

        lastPosition = topAdsProductList.lastOrNull()?.position ?: 0

        viewUpdater.removeLoading()
        viewUpdater.appendItems(topAdsProductList)
        onSuccessListener()
    }

    fun clearData() {
        lastPosition = 0
        canLoadMore = true
    }

    data class SearchPageProductData(
        val dimension90: String,
        val productListType: String,
        val externalReference: String,
        val keywordIntention: Int,
    )

    companion object {
        const val EXP_NAME = "ads_low_organic_sup"
        const val VAR_ADS = "var_ads"
    }
}
