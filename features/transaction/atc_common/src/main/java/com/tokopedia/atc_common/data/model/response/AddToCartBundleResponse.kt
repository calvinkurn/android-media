package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.SerializedName

data class AddToCartBundleGqlResponse(
    @SerializedName("add_to_cart_bundle")
    val addToCartBundle: AddToCartBundleResponse = AddToCartBundleResponse()
)

data class AddToCartBundleResponse(
    @SerializedName("data")
    val data: AddToCartBundleDataResponse = AddToCartBundleDataResponse(),
    @SerializedName("error_message")
    val errorMessage: String = "",
    @SerializedName("status")
    val status: String = ""
)

data class AddToCartBundleDataResponse(
    @SerializedName("data")
    val data: List<ProductDataResponse> = listOf(),
    @SerializedName("messages")
    val messages: List<String> = listOf(),
    @SerializedName("success")
    val success: Int = 0
)

data class ProductDataResponse(
    @SerializedName("cart_id")
    val cartId: String = "",
    @SerializedName("customer_id")
    val customerId: String = "",
    @SerializedName("notes")
    val notes: String = "",
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("quantity")
    val quantity: Int = 0,
    @SerializedName("shop_id")
    val shopId: String = "",
    @SerializedName("warehouse_id")
    val warehouseId: String = ""
)
