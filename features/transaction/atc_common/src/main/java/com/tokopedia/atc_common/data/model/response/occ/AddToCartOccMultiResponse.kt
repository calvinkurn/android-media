package com.tokopedia.atc_common.data.model.response.occ

import com.google.gson.annotations.SerializedName

data class AddToCartOccMultiGqlResponse(
        @SerializedName("add_to_cart_occ_multi")
        val addToCartOccMultiResponse: AddToCartOccMultiResponse = AddToCartOccMultiResponse()
)

data class AddToCartOccMultiResponse(
        @SerializedName("error_message")
        val errorMessage: ArrayList<String> = ArrayList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: DataOccMultiResponse = DataOccMultiResponse()
)

data class DataOccMultiResponse(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("message")
        val message: ArrayList<String> = arrayListOf(),
        @SerializedName("data")
        val detail: DetailOccMultiResponse = DetailOccMultiResponse()
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
        val warehouseId: String = "",
        //External Data
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("price")
        val price: String = "",
        @SerializedName("category")
        val category: String = "",
        @SerializedName("shop_type")
        val shopType: String = "",
        @SerializedName("shop_name")
        val shop_name: String = "",
        @SerializedName("picture")
        val picture: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("brand")
        val brand: String = "",
        @SerializedName("category_id")
        val categoryId: String = "",
        @SerializedName("variant")
        val variant: String = "",
        @SerializedName("tracker_attribution")
        val trackerAttribution: String = "",
        @SerializedName("is_multi_origin")
        val isMultiOrigin: Boolean = false,
        @SerializedName("is_free_ongkir")
        val isFreeOngkir: Boolean = false,
        @SerializedName("is_free_ongkir_extra")
        val isFreeOngkirExtra: Boolean = false
)

data class OccMultiCategoryResponse(
        @SerializedName("category_id")
        val categoryId: String = "",
        @SerializedName("category_name")
        val categoryName: String = "",
        @SerializedName("category_level")
        val categoryLevel: Int = 0
)