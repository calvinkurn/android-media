package com.tokopedia.search.result.product.ads

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.common.helper.isPostProcessingFilter
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.byteio.ByteIOTrackingData
import com.tokopedia.search.result.product.byteio.ByteIOTrackingDataFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsModel
import timber.log.Timber

class AdsInjector {

    private var topAdsPosition = 1

    fun resetTopAdsPosition() { topAdsPosition = 1 }

    fun injectAds(
        productList: List<ProductItemDataView>,
        adsModel: TopAdsModel,
        searchParameter: Map<String, Any>,
        isPostProcessing: Boolean,
        dimension90: String,
        productListType: String,
        externalReference: String,
        keywordIntention: Int,
        showButtonAtc: Boolean,
        isFirstPage: Boolean,
        byteIOTrackingDataFactory: ByteIOTrackingDataFactory,
    ): List<Visitable<*>> {
        val list = mutableListOf<ProductItemDataView>()
        list.addAll(productList)

        var topAdsIndex = 0
        val totalProductAds = adsModel.data?.size ?: 0
        val totalProductItem = productList.size + totalProductAds

        for (i in 0 until totalProductItem) {
            try {
                val willInjectAds = willInjectAds(adsModel, i, searchParameter, isPostProcessing)
                if (!willInjectAds) continue

                val topAds = adsModel.data[topAdsIndex]
                val item = ProductItemDataView.create(
                    topAds,
                    topAdsPosition,
                    dimension90,
                    productListType,
                    externalReference,
                    keywordIntention,
                    showButtonAtc,
                    byteIOTrackingDataFactory.create(isFirstPage)
                )

                list.add(i, item)
                topAdsIndex++
                topAdsPosition++
            } catch (e: java.lang.Exception) {
                Timber.w(e)
            }
        }

        return list
    }

    private fun willInjectAds(
        adsModel: TopAdsModel,
        index: Int,
        searchParameter: Map<String, Any>,
        isPostProcessing: Boolean,
    ) = isInjectAdsByTemplate(adsModel, index)
        || isPostProcessingFilter(searchParameter)
        || isPostProcessing

    private fun isInjectAdsByTemplate(adsModel: TopAdsModel, index: Int) =
        adsModel.templates.size > 0
            && adsModel.templates[index].isIsAd
}
