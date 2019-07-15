package com.tokopedia.atc_common.domain.model.response

/**
 * Created by Irfan Khoirul on 2019-07-15.
 */

data class DataModel(
        var success: Int = 0,
        var cartId: Long = 0,
        var productId: Int = 0,
        var quantity: Int = 0,
        var notes: String = "",
        var shopId: Int = 0,
        var customerId: Int = 0,
        var warehouseId: Int = 0,
        var trackerAttribution: String = "",
        var trackerListName: String = "",
        var ucUtParam: String = "",
        var isTradeIn: Boolean = false,
        var message: ArrayList<String> = arrayListOf()
)