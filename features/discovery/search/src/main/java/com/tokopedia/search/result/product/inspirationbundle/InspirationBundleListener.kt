package com.tokopedia.search.result.product.inspirationbundle

import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel

interface InspirationBundleListener {
    fun onSeeBundleClicked(
        bundle: InspirationProductBundleDataView.BundleDataView,
        selectedProducts: List<BundleProductUiModel>,
    )
    fun onBundleImpressed(
        bundle: InspirationProductBundleDataView.BundleDataView,
    )
    fun onBundleProductImpressed(
        bundle: InspirationProductBundleDataView.BundleDataView,
        bundleProduct: BundleProductUiModel,
    )
    fun onBundleProductClicked(
        bundle: InspirationProductBundleDataView.BundleDataView,
        bundleProduct: BundleProductUiModel,
    )
}