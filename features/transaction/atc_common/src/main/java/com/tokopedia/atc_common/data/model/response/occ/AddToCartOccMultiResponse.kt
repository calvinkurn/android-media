package com.tokopedia.atc_common.data.model.response.occ

import com.google.gson.annotations.SerializedName

data class AddToCartOccMultiGqlResponse(
        @SerializedName("add_to_cart_occ_multi")
        val response: AddToCartOccMultiResponse = AddToCartOccMultiResponse()
)

data class AddToCartOccMultiResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: DataOccMultiResponse = DataOccMultiResponse()
)

data class DataOccMultiResponse(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("message")
        val message: List<String> = emptyList(),
        @SerializedName("carts")
        val detail: List<DetailOccMultiResponse> = emptyList()
)

data class DetailOccMultiResponse(
        @SerializedName("cart_id")
        val cartId: String = "",
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("quantity")
        val quantity: String = "",
        @SerializedName("categories")
        val categories: List<OccMultiCategoryResponse> = emptyList(),
        @SerializedName("notes")
        val notes: String = "",
        @SerializedName("shop_id")
        val shopId: String = "",
        @SerializedName("customer_id")
        val customerId: String = "",
        @SerializedName("warehouse_id")
        val warehouseId: String = ""
)

data class OccMultiCategoryResponse(
        @SerializedName("category_id")
        val categoryId: String = "",
        @SerializedName("category_name")
        val categoryName: String = "",
        @SerializedName("category_level")
        val categoryLevel: Int = 0
)