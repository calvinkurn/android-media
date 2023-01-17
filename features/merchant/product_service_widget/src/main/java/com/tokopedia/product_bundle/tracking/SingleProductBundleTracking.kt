package com.tokopedia.product_bundle.tracking

import com.tokopedia.common.ProductServiceWidgetConstant.TrackerId.ADD_TO_CART_BUNDLING
import com.tokopedia.common.ProductServiceWidgetConstant.TrackerId.CLICK_BACK
import com.tokopedia.common.ProductServiceWidgetConstant.TrackerId.CLICK_BUNDLE_OPTIONS
import com.tokopedia.product_bundle.multiple.presentation.model.ProductDetailBundleTracker

object SingleProductBundleTracking: BaseProductBundleTracking() {

    private const val BUNDLING_TYPE = "single"

    fun trackSingleBundleOptionClick(
        bundleId: String?,
        parentProductId: String,
        selectedProductId: String?
    ) {
        if (bundleId != null) {
            // selectedProductId null means variant not selected yet
            val productId = selectedProductId ?: parentProductId
            super.trackBundleOptionClick(
                "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;",
                productId,
                CLICK_BUNDLE_OPTIONS
            )
        }
    }

    fun trackSingleBuyClick(
            userId: String,
            source: String,
            parentProductId: String,
            bundleId: String?,
            selectedProductId: String?,
            shopId: String,
            productIds: String,
            productDetails: List<ProductDetailBundleTracker>,
    ) {
        if (bundleId != null) {
            // selectedProductId null means variant not selected yet
            val productId = selectedProductId ?: parentProductId

            trackBuyClick(
                    userId = userId,
                    label = "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;",
                    productIds = productIds,
                    source = source,
                    bundleType = VALUE_SINGLE_BUNDLING,
                    trackerId = ADD_TO_CART_BUNDLING,
                    productDetails = productDetails,
                    bundleId = bundleId,
                    shopId = shopId
            )
        }
    }

    fun trackSingleBackClick(
        bundleId: String?,
        parentProductId: String,
        selectedProductId: String?
    ) {
        if (bundleId != null) {
            // selectedProductId null means variant not selected yet
            val productId = selectedProductId ?: parentProductId
            super.trackBackClick(
                "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;",
                productId,
                CLICK_BACK
            )
        }
    }

}
