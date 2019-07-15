package com.tokopedia.atc_common.data.model.request

/**
 * Created by Irfan Khoirul on 2019-07-10.
 */

data class AddToCartRequest(
        var productId: Long = 0,
        var shopId: Int = 0,
        var quantity: Int = 0,
        var notes: String = "",
        var lang: String = "",
        var attribution: String = "",
        var listTracker: String = "",
        var ucParams: String = "",
        var warehouseId: Int = 0,
        var atcFromExternalSource: String = "",
        var isSCP: Boolean = false
) {
    companion object {
        val ATC_FROM_WISHLIST = "wishlist_list"
        val ATC_FROM_RECENT_VIEW = "last_seen_list"
        val ATC_FROM_RECOMMENDATION = "recommendation_list"
    }
}