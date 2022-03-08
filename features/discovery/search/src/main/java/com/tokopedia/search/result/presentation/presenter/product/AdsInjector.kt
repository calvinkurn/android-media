package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.IS_FULFILLMENT
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.topads.sdk.domain.model.TopAdsModel
import timber.log.Timber

class AdsInjector {

    private var topAdsPosition = 1

    fun resetTopAdsPosition() { topAdsPosition = 1 }

    fun injectAds(
        productList: List<ProductItemDataView>,
        adsModel: TopAdsModel,
        searchParameter: Map<String, Any>,
        dimension90: String,
    ): List<Visitable<*>> {
        val list = mutableListOf<ProductItemDataView>()
        list.addAll(productList)

        var topAdsIndex = 0
        val totalProductAds = adsModel.data?.size ?: 0
        val totalProductItem = productList.size + totalProductAds

        for (i in 0 until totalProductItem) {
            try {
                val willInjectAds = willInjectAds(adsModel, i, searchParameter)
                if (!willInjectAds) continue

                val topAds = adsModel.data[topAdsIndex]
                val item = ProductItemDataView.create(topAds, topAdsPosition, dimension90)

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
    ) = isInjectAdsByTemplate(adsModel, index)
        || isIgnoreTemplate(searchParameter)

    private fun isInjectAdsByTemplate(adsModel: TopAdsModel, index: Int) =
        adsModel.templates.size > 0
            && adsModel.templates[index].isIsAd

    private fun isIgnoreTemplate(searchParameter: Map<String, Any>) =
        searchParameter[IS_FULFILLMENT].toString().toBoolean()
}