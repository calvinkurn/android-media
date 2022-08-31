package com.tokopedia.search.result.product.inspirationbundle

import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel

interface InspirationBundleListener {
    fun onSeeBundleClicked(
        bundle: InspirationProductBundleDataView.Bundle,
        selectedProducts: List<BundleProductUiModel>,
    )
    fun onBundleImpressed(
        bundle: InspirationProductBundleDataView.Bundle,
    )
    fun onBundleProductClicked(
        bundle: InspirationProductBundleDataView.Bundle,
        bundleProduct: BundleProductUiModel,
    )
}