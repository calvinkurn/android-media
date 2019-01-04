package com.tokopedia.trackingoptimizer.model

/**
 * Created by hendry on 26/12/18.
 */
data class ScreenCustomModel(var shopId: String?,
                             var shopType: String?,
                             var pageType: String?,
                             var productId: String?) {
    fun hasCustom(): Boolean {
        return !shopId.isNullOrEmpty() &&
                !shopType.isNullOrEmpty() &&
                !pageType.isNullOrEmpty() &&
                !productId.isNullOrEmpty()
    }
}