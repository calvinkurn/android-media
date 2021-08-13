package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-07-12.
 */

data class DataResponse(
        @SerializedName("success")
        @Expose
        val success: Int = 0,

        @SerializedName("cart_id")
        @Expose
        val cartId: String = "",

        @SerializedName("product_id")
        @Expose
        val productId: Long = 0,

        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0,

        @SerializedName("notes")
        @Expose
        val notes: String = "",

        @SerializedName("shop_id")
        @Expose
        val shopId: Long = 0,

        @SerializedName("customer_id")
        @Expose
        val customerId: Long = 0,

        @SerializedName("warehouse_id")
        @Expose
        val warehouseId: Long = 0,

        @SerializedName("tracker_attribution")
        @Expose
        val trackerAttribution: String = "",

        @SerializedName("tracker_list_name")
        @Expose
        val trackerListName: String = "",

        @SerializedName("uc_ut_param")
        @Expose
        val ucUtParam: String = "",

        @SerializedName("is_trade_in")
        @Expose
        val isTradeIn: Boolean = false,

        @SerializedName("message")
        @Expose
        val message: ArrayList<String> = arrayListOf()
)