package com.tokopedia.product_bundle.tracking

import com.tokopedia.common.ProductServiceWidgetConstant.TrackerId.ADD_TO_CART_BUNDLING
import com.tokopedia.common.ProductServiceWidgetConstant.TrackerId.CLICK_BACK
import com.tokopedia.common.ProductServiceWidgetConstant.TrackerId.CLICK_BUNDLE_OPTIONS
import com.tokopedia.common.ProductServiceWidgetConstant.TrackerId.CLICK_CHOOSE_PRODUCT_VARIANT
import com.tokopedia.common.ProductServiceWidgetConstant.TrackerId.CLICK_SEE_PRODUCT
import com.tokopedia.product_bundle.multiple.presentation.model.ProductDetailBundleTracker

object MultipleProductBundleTracking : BaseProductBundleTracking() {

    private const val BUNDLING_TYPE = "multiple"

    fun trackMultipleBundleOptionClick(bundleId: String, productId: String) {
        trackBundleOptionClick(
            label = "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;",
            productId = productId,
            trackerId = CLICK_BUNDLE_OPTIONS
        )
    }

    fun trackMultiplePreviewProductClick(
        selectedProductId: String,
        bundleId: String,
        productId: String
    ) {
        trackPreviewProductClick(
            label = "product_id:$selectedProductId; bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;",
            productId = productId,
            trackerId = CLICK_SEE_PRODUCT
        )
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
            label = "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE; product_id:$selectedProductId; level : $variantLevel; variant_title : $variantTitle; variant_value : $variantValue; variant : $variantProductId;",
            productId = productId,
            trackerId = CLICK_CHOOSE_PRODUCT_VARIANT
        )
    }

    fun trackMultipleBuyClick(
        userId: String,
        bundleId: String,
        productIds: String,
        source: String,
        productDetails: List<ProductDetailBundleTracker>,
        shopId: String
    ) {
        trackBuyClick(
            userId = userId,
            label = "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;",
            productIds = productIds,
            source = source,
            bundleType = VALUE_MULTIPLE_BUNDLING,
            trackerId = ADD_TO_CART_BUNDLING,
            productDetails = productDetails,
            bundleId = bundleId,
            shopId = shopId
        )

    }

    fun trackMultipleBackClick(bundleId: String, productId: String) {
        trackBackClick(
            label = "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;",
            productId = productId,
            trackerId = CLICK_BACK
        )
    }

}
