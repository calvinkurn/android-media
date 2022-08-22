package com.tokopedia.product_bundle.tracking

import com.tokopedia.product_bundle.common.data.model.uimodel.AddToCartDataResult

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
                productId
            )
        }
    }

    fun trackSingleBuyClick(
            userId: String,
            atcResult: AddToCartDataResult,
            source: String,
            bundleName: String,
            bundlePrice: Long,
            parentProductId: String,
            bundleId: String?,
            selectedProductId: String?
    ) {
        if (bundleId != null) {
            // selectedProductId null means variant not selected yet
            val productId = selectedProductId ?: parentProductId

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

//            super.trackBuyClick(
//                "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;",
//                productId
//            )
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
                productId
            )
        }
    }

}