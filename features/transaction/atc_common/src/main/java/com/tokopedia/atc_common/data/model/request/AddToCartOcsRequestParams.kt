package com.tokopedia.atc_common.data.model.request

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-07-15.
 */

data class AddToCartOcsRequestParams(
        @SerializedName("product_id")
        var productId: Long,

        @SerializedName("shop_id")
        var shopId: Int,

        @SerializedName("quantity")
        var quantity: Int,

        @SerializedName("notes")
        var notes: Int,

        @SerializedName("warehouse_id")
        var warehouseId: Int,

        @SerializedName("customer_id")
        var customerId: Int,

        @SerializedName("tracker_attribution")
        var trackerAttribution: String,

        @SerializedName("tracker_list_name")
        var trackerListName: String,

        @SerializedName("ut_param")
        var utParam: String,

        @SerializedName("is_trade_in")
        var isTradeIn: Boolean
)