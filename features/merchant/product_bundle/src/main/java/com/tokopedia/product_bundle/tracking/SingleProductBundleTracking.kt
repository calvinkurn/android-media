package com.tokopedia.product_bundle.tracking

object SingleProductBundleTracking: BaseProductBundleTracking() {

    private const val BUNDLING_TYPE = "single"

    fun trackSingleBundleOptionClick(bundleId:String?, productId: String) {
        if (bundleId != null) {
            super.trackBundleOptionClick(
                "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;",
                productId
            )
        }
    }

    fun trackSingleBuyClick(bundleId: String?, productId: String) {
        if (bundleId != null) {
            super.trackBuyClick(
                "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;",
                productId
            )
        }
    }

    fun trackSingleBackClick(bundleId: String?, productId: String) {
        if (bundleId != null) {
            super.trackBackClick(
                "bundling_id:$bundleId; bundling_type:$BUNDLING_TYPE;",
                productId
            )
        }
    }

}