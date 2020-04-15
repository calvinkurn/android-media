package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DetailOccResponse(
        @SerializedName("cart_id")
        @Expose
        val cartId: Int = 0,

        @SerializedName("product_id")
        @Expose
        val productId: Int = 0,

        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0,

        @SerializedName("notes")
        @Expose
        val notes: String = "",

        @SerializedName("shop_id")
        @Expose
        val shopId: Int = 0,

        @SerializedName("customer_id")
        @Expose
        val customerId: Int = 0,

        @SerializedName("warehouse_id")
        @Expose
        val warehouseId: Int = 0,

        @SerializedName("is_trade_in")
        @Expose
        val isTradeIn: Boolean = false,

        @SerializedName("is_scp")
        @Expose
        val isScp: Boolean = false
)