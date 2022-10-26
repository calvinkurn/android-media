package com.tokopedia.productbundlewidget.listener

import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productbundlewidget.model.BundleUiModel

interface ProductBundleWidgetListener {

    fun onBundleProductClicked(
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel
    )

    fun onMultipleBundleActionButtonClicked(
        selectedBundle: BundleDetailUiModel,
        productDetails: List<BundleProductUiModel>
    )

    fun onSingleBundleActionButtonClicked(
        selectedBundle: BundleDetailUiModel,
        bundleProducts: BundleProductUiModel
    )

    fun onSingleBundleChipsSelected(
        selectedProduct: BundleProductUiModel,
        selectedBundle: BundleDetailUiModel,
        bundleName: String
    )

    fun impressionSingleBundle(
        selectedBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        bundleName: String
    )

    fun impressionMultipleBundle(
        selectedMultipleBundle: BundleDetailUiModel,
        bundlePosition: Int
    )

    fun impressionMultipleBundleProduct(
        selectedProduct: BundleProductUiModel,
        selectedBundle: BundleDetailUiModel
    )
}
