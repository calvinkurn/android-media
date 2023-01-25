package com.tokopedia.atc_common.domain.model.request

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 04/11/20.
 */
data class AddToCartMultiParam(
        @SerializedName("product_id")
        val productId: String = "",

        @SerializedName("product_name")
        val productName: String = "",

        @SerializedName("product_price")
        val productPrice: Double = 0.0,

        @SerializedName("quantity")
        val qty: Int = -1,

        @SerializedName("notes")
        val notes: String = "",

        @SerializedName("shop_id")
        val shopId: String = "",

        @SerializedName("customer_id")
        val custId: String = "",

        @SerializedName("warehouse_id")
        val warehouseId: String = "0",

        @SerializedName("category")
        val category: String = ""
)
