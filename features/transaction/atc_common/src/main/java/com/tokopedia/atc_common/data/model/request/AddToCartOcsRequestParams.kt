package com.tokopedia.atc_common.data.model.request

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-07-15.
 */

data class AddToCartOcsRequestParams(
        @SerializedName("product_id")
        var productId: Long = 0,

        @SerializedName("shop_id")
        var shopId: Int = 0,

        @SerializedName("quantity")
        var quantity: Int = 0,

        @SerializedName("notes")
        var notes: String = "",

        @SerializedName("warehouse_id")
        var warehouseId: Int = 0,

        @SerializedName("customer_id")
        var customerId: Int = 0,

        @SerializedName("tracker_attribution")
        var trackerAttribution: String = "",

        @SerializedName("tracker_list_name")
        var trackerListName: String = "",

        @SerializedName("ut_param")
        var utParam: String = "",

        @SerializedName("is_trade_in")
        var isTradeIn: Boolean = false
)