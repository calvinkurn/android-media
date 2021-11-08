package com.tokopedia.atc_common.data.model.response.atcexternal

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class AddToCartOccMultiExternalGqlResponse(
        @SerializedName("add_to_cart_occ_external")
        val response: AddToCartOccMultiExternalResponse = AddToCartOccMultiExternalResponse()
)

data class AddToCartOccMultiExternalResponse(
        @SerializedName("data")
        val data: OccMultiExternalDataResponse = OccMultiExternalDataResponse(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList()
)

data class OccMultiExternalDataResponse(
        @SerializedName("message")
        val message: List<String> = emptyList(),
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("carts")
        val data: List<AddToCartOccMultiExternalDataResponse> = emptyList()
)

data class AddToCartOccMultiExternalDataResponse(
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("quantity")
        val quantity: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        val price: Double = 0.0,
        @SerializedName("category")
        val category: String = "",
        @SerializedName("shop_id")
        val shopId: String = "",
        @SerializedName("shop_type")
        val shopType: String = "",
        @SerializedName("shop_name")
        val shopName: String = "",
        @SerializedName("picture")
        val picture: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("cart_id")
        val cartId: String = "",
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