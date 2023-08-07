package com.tokopedia.search.result.product.ads

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressPresenterDelegate
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.search.result.product.responsecode.ResponseCodeProvider
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
    responseCodeProvider: ResponseCodeProvider,
): ResponseCodeProvider by responseCodeProvider {

    private val isEnabledRollence by lazy(LazyThreadSafetyMode.NONE, ::getIsEnabledRollence)

    val isEnabled: Boolean
        get() = isAdsLowOrganicAllowed

    private fun getIsEnabledRollence() = try {
        abTestRemoteConfig.get().getString(EXP_NAME, "") == VAR_ADS
    } catch (ignored: Throwable) {
        false
    }

    private var injectCount = 0
    private var lastPosition = 0
    private var canLoadMore = true

    private val isBelowInjectLimit: Boolean
        get() = injectCount < ADS_INJECT_LIMIT

    private val isExperimentResponseCode: Boolean
        get() = responseCode in experimentResponseCode

    private val isRollOutResponseCode: Boolean
        get() = responseCode in rollOutResponseCode

    private val isAdsLowOrganicAllowed: Boolean
        get() = (isExperimentResponseCode && isEnabledRollence) || isRollOutResponseCode

    fun processAdsLowOrganic(
        isHideProductAds: Boolean,
        keyword: String,
        topAdsModel: TopAdsModel,
        productData: SearchPageProductData,
        action: (List<Visitable<*>>) -> Unit,
    ) {
        if (isHideProductAds || !isAdsLowOrganicAllowed || topAdsModel.data.isEmpty() || !isBelowInjectLimit) return

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
        val keyword = searchParameter[SearchApiConst.Q] as? String ?: ""

        topAdsUseCase.execute(
            requestParams,
            topAdsNextPageSubscriber(productData, onSuccessListener, keyword)
        )
    }

    private fun canLoadNextPage() = isAdsLowOrganicAllowed && canLoadMore && isBelowInjectLimit

    private fun topAdsNextPageSubscriber(
        productData: SearchPageProductData,
        onSuccessListener: () -> Unit,
        keyword: String,
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
                keyword,
            )
        }
    }

    private fun List<Visitable<*>>?.hasNoAdsLowOrganicTitle() : Boolean {
        return this?.none { it is AdsLowOrganicTitleDataView } ?: false
    }

    private fun onTopAdsNextPageSuccessful(
        topAdsModel: TopAdsModel,
        productData: SearchPageProductData,
        onSuccessListener: () -> Unit,
        keyword: String,
    ) {
        if (topAdsModel.data.isEmpty()) {
            canLoadMore = false
            viewUpdater.removeLoading()
            return
        }

        val topAdsProductList = createTopAdsProductList(topAdsModel, productData)

        lastPosition = topAdsProductList.lastOrNull()?.position ?: 0

        viewUpdater.removeLoading()
        val isAdsLowOrganicTitleNotExist = viewUpdater.itemList.hasNoAdsLowOrganicTitle()
        if (isAdsLowOrganicTitleNotExist) {
            viewUpdater.appendItems(listOf(AdsLowOrganicTitleDataView.create(keyword)))
        }
        viewUpdater.appendItems(topAdsProductList)
        injectCount++
        onSuccessListener()
    }

    fun clearData() {
        injectCount = 0
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
        const val EXP_NAME = "exp_alos_req"
        const val VAR_ADS = "var1"

        private const val ADS_INJECT_LIMIT = 2

        private val rollOutResponseCode = listOf("0", "1")
        private val experimentResponseCode = listOf("4", "5",)
    }
}
