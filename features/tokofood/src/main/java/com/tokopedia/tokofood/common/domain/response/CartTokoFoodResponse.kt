package com.tokopedia.tokofood.common.domain.response

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFood

data class CartTokoFoodResponse(
    @SerializedName("success")
    @Expose
    val success: Int = 0,
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("data")
    @Expose
    val data: CartTokoFoodData = CartTokoFoodData()
)

data class CartTokoFoodData(
    @SerializedName("carts")
    @Expose
    val carts: List<CartTokoFood> = listOf()
)

data class CartTokoFood(
    @SerializedName("success")
    @Expose
    val success: Int = 0,
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("business_id")
    @Expose
    val businessId: String = "",
    @SerializedName("cart_id")
    @Expose
    val cartId: String = "",
    @SerializedName("product_id")
    @Expose
    val productId: String = "",
    @SerializedName("shop_id")
    @Expose
    val shopId: String = "",
    @SerializedName("quantity")
    @Expose
    val quantity: Int = 0,
    @SerializedName("metadata")
    @Expose
    val metadata: String = ""
) {
    fun getMetadata(): CartMetadataTokoFood {
        return Gson().fromJson(metadata, CartMetadataTokoFood::class.java)
    }

}