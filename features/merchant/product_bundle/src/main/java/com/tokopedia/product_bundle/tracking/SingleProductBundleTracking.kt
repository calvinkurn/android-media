package com.tokopedia.product_bundle.tracking

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
        bundleId: String?,
        parentProductId: String,
        selectedProductId: String?
    ) {
        if (bundleId != null) {
            // selectedProductId null means variant not selected yet
            val productId = selectedProductId ?: parentProductId
            super.trackBuyClick(
                "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;",
                productId
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
                productId
            )
        }
    }

}