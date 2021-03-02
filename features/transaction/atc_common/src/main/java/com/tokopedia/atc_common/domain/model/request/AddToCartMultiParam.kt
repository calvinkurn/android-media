package com.tokopedia.atc_common.domain.model.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.atc_common.data.model.request.chosenaddress.ChosenAddressAddToCart

/**
 * Created by fwidjaja on 04/11/20.
 */
data class AddToCartMultiParam(
        @SerializedName("product_id")
        val productId: Long = -1,

        @SerializedName("product_name")
        val productName: String = "",

        @SerializedName("product_price")
        val productPrice: Long = -1,

        @SerializedName("quantity")
        val qty: Int = -1,

        @SerializedName("notes")
        val notes: String = "",

        @SerializedName("shop_id")
        val shopId: Int = -1,

        @SerializedName("customer_id")
        val custId: Int = -1,

        @SerializedName("warehouse_id")
        val warehouseId: Int = -1,

        @SerializedName("category")
        val category: String = "",

        @SerializedName("chosen_address")
        var chosenAddress: ChosenAddressAddToCart? = null
)