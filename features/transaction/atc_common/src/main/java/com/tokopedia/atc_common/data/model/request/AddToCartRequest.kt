package com.tokopedia.atc_common.data.model.request

/**
 * Created by Irfan Khoirul on 2019-07-10.
 */

data class AddToCartRequest(
        val productId: Long = 0,
        val shopId: Int = 0,
        val quantity: Int = 0,
        val notes: String = "",
        val lang: String = "",
        val attribution: String = "",
        val listTracker: String = "",
        val ucParams: String = "",
        val warehouseId: Int = 0,
        val atcFromExternalSource: String = "",
        val isSCP: Boolean = false
)