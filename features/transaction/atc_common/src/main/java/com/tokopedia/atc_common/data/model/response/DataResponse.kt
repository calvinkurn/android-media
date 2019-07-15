package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-07-12.
 */

data class DataResponse(
        @SerializedName("success")
        @Expose
        val success: Int,

        @SerializedName("cart_id")
        @Expose
        val cartId: Long,

        @SerializedName("product_id")
        @Expose
        val productId: Int,

        @SerializedName("quantity")
        @Expose
        val quantity: Int,

        @SerializedName("notes")
        @Expose
        val notes: String,

        @SerializedName("shop_id")
        @Expose
        val shopId: Int,

        @SerializedName("customer_id")
        @Expose
        val customerId: Int,

        @SerializedName("warehouse_id")
        @Expose
        val warehouseId: Int,

        @SerializedName("tracker_attribution")
        @Expose
        val trackerAttribution: String,

        @SerializedName("tracker_list_name")
        @Expose
        val trackerListName: String,

        @SerializedName("uc_ut_param")
        @Expose
        val ucUtParam: String,

        @SerializedName("is_trade_in")
        @Expose
        val isTradeIn: Boolean,

        @SerializedName("message")
        @Expose
        val message: ArrayList<String>
)