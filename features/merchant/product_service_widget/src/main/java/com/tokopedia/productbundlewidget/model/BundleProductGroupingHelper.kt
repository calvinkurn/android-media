package com.tokopedia.productbundlewidget.model

import com.tokopedia.productbundlewidget.adapter.constant.ProductBundleConstant.MAX_PRODUCT_DISPLAYED

object BundleProductGroupingHelper {
    fun groupDataSet(
        bundleProducts: List<BundleProductUiModel>
    ): Pair<List<BundleProductUiModel>, List<BundleProductUiModel>> {
        if (bundleProducts.size <= MAX_PRODUCT_DISPLAYED) return Pair(bundleProducts, emptyList())
        val bundleProductsDisplayed: MutableList<BundleProductUiModel> = mutableListOf()
        val bundleProductsGrouped: MutableList<BundleProductUiModel> = mutableListOf()
        bundleProducts.forEachIndexed { index, bundleProductUiModel ->
            if (index < MAX_PRODUCT_DISPLAYED.dec()) {
                bundleProductsDisplayed.add(bundleProductUiModel)
            } else {
                bundleProductsGrouped.add(bundleProductUiModel)
            }
        }
        return Pair(bundleProductsDisplayed, bundleProductsGrouped)
    }
}
