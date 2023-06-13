package com.tokopedia.cart.view.bottomsheet

import com.tokopedia.productbundlewidget.model.BundleDetailUiModel

interface CartBundlingBottomSheetListener {

    fun onNewBundleProductAddedToCart()

    fun onMultipleBundleActionButtonClicked(
        selectedBundle: BundleDetailUiModel
    )

    fun onSingleBundleActionButtonClicked(
        selectedBundle: BundleDetailUiModel
    )

    fun impressionMultipleBundle(
        selectedMultipleBundle: BundleDetailUiModel
    )

    fun impressionSingleBundle(
        selectedBundle: BundleDetailUiModel
    )
}
