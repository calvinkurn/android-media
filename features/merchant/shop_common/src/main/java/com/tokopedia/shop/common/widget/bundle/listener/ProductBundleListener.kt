package com.tokopedia.shop.common.widget.bundle.listener

import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel

interface ProductBundleListener {

    fun onBundleProductClicked(
        bundleType: BundleTypes,
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        productItemPosition: Int
    )

    fun addMultipleBundleToCart(
        selectedMultipleBundle: BundleDetailUiModel,
        productDetails: List<BundleProductUiModel>
    )

    fun addSingleBundleToCart(
        selectedBundle: BundleDetailUiModel,
        bundleProducts: BundleProductUiModel
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

    fun impressionProductItemBundleMultiple(
        selectedProduct: BundleProductUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        productItemPosition: Int
    )
}