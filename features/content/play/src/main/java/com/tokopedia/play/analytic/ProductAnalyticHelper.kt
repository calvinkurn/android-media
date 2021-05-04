package com.tokopedia.play.analytic

import com.tokopedia.play.view.uimodel.PlayProductUiModel


/**
 * Created by mzennis on 20/04/21.
 */
class ProductAnalyticHelper(
        private val analytic: PlayAnalytic
) {

    @TrackingField
    private val impressedProducts = mutableListOf<Pair<PlayProductUiModel.Product, Int>>()

    fun trackImpressedProducts(products: List<Pair<PlayProductUiModel.Product, Int>>) {
        if (products.isNotEmpty()) impressedProducts.addAll(products)
    }

    fun sendImpressedBottomSheetProducts() {
        analytic.impressBottomSheetProducts(getFinalProducts())
        clear()
    }

    fun sendImpressedFeaturedProducts() {
        analytic.impressFeaturedProducts(getFinalProducts())
        clear()
    }

    private fun getFinalProducts() = impressedProducts.distinctBy { it.first.id }

    private fun clear() {
        impressedProducts.clear()
    }
}