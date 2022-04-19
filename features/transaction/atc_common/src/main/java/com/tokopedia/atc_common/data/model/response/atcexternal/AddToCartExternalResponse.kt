package com.tokopedia.atc_common.data.model.response.atcexternal

import com.google.gson.annotations.SerializedName

data class AddToCartExternalGqlResponse(
        @SerializedName("add_to_cart_external_v2")
        val response: AddToCartExternalResponse = AddToCartExternalResponse()
)

data class AddToCartExternalResponse(
        @SerializedName("data")
        val data: DataResponse = DataResponse(),
        @SerializedName("status")
        val status: String = ""
)

data class DataResponse(
        @SerializedName("message")
        val message: List<String> = emptyList(),
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("data")
        val data: AddToCartExternalDataResponse = AddToCartExternalDataResponse()
)

data class AddToCartExternalDataResponse(
        @SerializedName("product_id")
        val productId: Long = 0,
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("quantity")
        val quantity: Int = 0,
        @SerializedName("price")
        val price: Int = 0,
        @SerializedName("category")
        val category: String = "",
        @SerializedName("shop_id")
        val shopId: Long = 0,
        @SerializedName("shop_type")
        val shopType: String = "",
        @SerializedName("shop_name")
        val shopName: String = "",
        @SerializedName("picture")
        val picture: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("cart_id")
        val cartId: Long = 0,
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