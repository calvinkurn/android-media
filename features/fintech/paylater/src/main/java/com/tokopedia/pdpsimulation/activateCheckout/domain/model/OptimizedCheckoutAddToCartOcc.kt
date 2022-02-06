package com.tokopedia.pdpsimulation.activateCheckout.domain.model

import com.google.gson.annotations.SerializedName

class OptimizedCheckoutAddToCartOcc(
        @SerializedName("add_to_cart_occ_multi")
        val response: AddToCartOccMultiResponse = AddToCartOccMultiResponse()
)

class AddToCartOccMultiResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: DataOccMultiResponse = DataOccMultiResponse()
)

class DataOccMultiResponse(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("message")
        val message: List<String> = emptyList(),
        @SerializedName("carts")
        val detail: List<DetailOccMultiResponse> = emptyList(),

)

class DetailOccMultiResponse(
        @SerializedName("cart_id")
        val cartId: String = "",
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("quantity")
        val quantity: String = "",
        @SerializedName("notes")
        val notes: String = "",
        @SerializedName("shop_id")
        val shopId: String = "",
        @SerializedName("customer_id")
        val customerId: String = "",
        @SerializedName("warehouse_id")
        val warehouseId: String = ""
)