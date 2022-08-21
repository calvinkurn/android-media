package com.tokopedia.product_bundle.tracking

import com.tokopedia.product_bundle.common.data.model.uimodel.AddToCartDataResult

object MultipleProductBundleTracking: BaseProductBundleTracking() {

    private const val BUNDLING_TYPE = "multiple"

    fun trackMultipleBundleOptionClick(bundleId: String, productId: String) {
        trackBundleOptionClick("bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;", productId)
    }

    fun trackMultiplePreviewProductClick(selectedProductId: String, bundleId: String, productId: String) {
        trackPreviewProductClick("product_id:$selectedProductId; bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;", productId)
    }

    fun trackMultipleSelectVariantClick(
        selectedProductId: String,
        bundleId: String,
        variantLevel: String,
        variantTitle: String,
        variantValue: String,
        variantProductId: String,
        productId: String
    ) {
        trackSelectVariantClick(
            "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE; product_id:$selectedProductId; level : $variantLevel; variant_title : $variantTitle; variant_value : $variantValue; variant : $variantProductId;",
            productId
        )
    }

    fun trackMultipleBuyClick(
            userId: String,
            bundleId: String,
            productId: String,
            atcResult: AddToCartDataResult,
            source: String,
            bundleName: String,
            bundlePrice: Long
    ) {
        trackBuyClick(
                userId = userId,
                label = "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;",
                productId = productId,
                atcResult = atcResult,
                source = source,
                bundleName = bundleName,
                bundleType = VALUE_MULTIPLE_BUNDLING,
                bundlePrice = bundlePrice
        )

//        trackBuyClick("bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;", productId)
    }

    fun trackMultipleBackClick(bundleId: String, productId: String) {
        trackBackClick("bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;", productId)
    }

}