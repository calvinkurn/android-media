package com.tokopedia.play.analytic

import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.PlayProductTagsUiModel


/**
 * Created by mzennis on 20/04/21.
 */
class ProductAnalyticHelper(
        private val analytic: PlayAnalytic
) {

    @TrackingField
    private val featuredProductMap = mutableListOf<Pair<PlayProductUiModel.Product, Boolean>>()

    @TrackingField
    private val bottomSheetProductMap = mutableMapOf<PlayProductUiModel.Product, Boolean>()

    fun setProductTags(productTags: PlayProductTagsUiModel) {
        if (productTags is PlayProductTagsUiModel.Complete) {
            val products = productTags.productList.filterIsInstance<PlayProductUiModel.Product>()
            if (products.isNotEmpty()) {
                handleFeaturedProduct(products, productTags.basicInfo.maxFeaturedProducts)
                handleBottomSheetProduct(products)
            }
        }
    }

    private fun handleFeaturedProduct(products: List<PlayProductUiModel.Product>, maxFeaturedProducts: Int) {
        val newProducts = products.take(maxFeaturedProducts).map { Pair(it, false) }
        if (featuredProductMap.isEmpty()) featuredProductMap.addAll(newProducts)
        else {
            val oldProducts = featuredProductMap
            val differ = (oldProducts + newProducts).groupBy { it.first.id }
                    .filter { it.value.size == 1 }
                    .flatMap { it.value }
            featuredProductMap.clear()
            featuredProductMap.addAll(differ)
        }
    }

    private fun handleBottomSheetProduct(newProducts: List<PlayProductUiModel.Product>) {
        if (bottomSheetProductMap.isEmpty()) bottomSheetProductMap.putAll(newProducts.map { it to false })
        else {

        }
    }

    fun sendImpressionFeaturedProduct(product: PlayProductUiModel.Product, position: Int) {
        featuredProductMap
                .find { it.first.id == product.id }
                .run {
                    if (this != null && !this.second) {
                        analytic.impressionFeaturedProduct(this.first, position)
                    }
                }

        val impressedProduct = featuredProductMap.find { it.first.id == product.id }
        if (impressedProduct != null && !impressedProduct.second) {
            analytic.impressionFeaturedProduct(product, position)
            featuredProductMap.map { it.first.id == impressedProduct.first.id }
        }
    }

    fun sendImpressionBottomSheetProduct() {

    }
}