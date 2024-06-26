package com.tokopedia.productbundlewidget.listener

import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productbundlewidget.model.BundleTypes
import com.tokopedia.productbundlewidget.model.BundleUiModel

interface ProductBundleAdapterListener {

    fun onBundleProductClicked(
        bundleType: BundleTypes,
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        itemPosition: Int
    )

    fun onMultipleBundleActionButtonClicked(
        selectedMultipleBundle: BundleDetailUiModel,
        productDetails: List<BundleProductUiModel>,
        bundlePosition: Int
    )

    fun onMultipleBundleMoreProductClicked(
        selectedMultipleBundle: BundleDetailUiModel,
        bundleProductGrouped: List<BundleProductUiModel>,
        bundleProductAll: List<BundleProductUiModel>
    )

    fun onSingleBundleActionButtonClicked(
        selectedBundle: BundleDetailUiModel,
        bundleProducts: BundleProductUiModel,
        bundlePosition: Int
    )

    fun onTrackSingleVariantChange(
        selectedProduct: BundleProductUiModel,
        selectedSingleBundle: BundleDetailUiModel,
        bundleName: String,
    )

    fun impressionProductBundleSingle(
        selectedSingleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        bundleName: String,
        bundlePosition: Int
    )

    fun impressionProductBundleMultiple(
        selectedMultipleBundle: BundleDetailUiModel,
        bundlePosition: Int,
    )

    fun impressionProductBundleMultiple(
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        bundlePosition: Int,
    )

    fun impressionProductItemBundleMultiple(
        selectedProduct: BundleProductUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        productItemPosition: Int
    )

    fun impressionProductItemBundleMultiple(
        bundle: BundleUiModel,
        selectedProduct: BundleProductUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        productItemPosition: Int
    )
}
