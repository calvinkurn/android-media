package com.tokopedia.tokofood.common.domain.response

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFood
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFoodWithVariant

data class AddToCartTokoFoodResponse(
    @SerializedName("add_to_cart_general")
    @Expose
    val cartResponse: CartTokoFoodResponse
)

data class RemoveCartTokoFoodResponse(
    @SerializedName("remove_cart_general")
    @Expose
    val cartResponse: CartTokoFoodResponse
)

data class UpdateCartTokoFoodResponse(
    @SerializedName("update_cart_general")
    @Expose
    val cartResponse: CartTokoFoodResponse
)

data class CartTokoFoodResponse(
    @SerializedName("status")
    @Expose
    val status: String = "",
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("data")
    @Expose
    val data: CartTokoFoodData = CartTokoFoodData()
) {
    fun isSuccess(): Boolean =
        status == TokoFoodCartUtil.SUCCESS_STATUS && data.success == TokoFoodCartUtil.SUCCESS_STATUS_INT
    fun getMessageIfError(): String {
        return when {
            status == TokoFoodCartUtil.ERROR_STATUS -> message
            data.success != TokoFoodCartUtil.SUCCESS_STATUS_INT -> data.message
            else -> String.EMPTY
        }
    }
}

data class CartTokoFoodData(
    @SerializedName("success")
    @Expose
    val success: Int = 0,
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("carts")
    @Expose
    val carts: List<CartTokoFood> = listOf(),
    @SerializedName("bottomsheet")
    @Expose
    val bottomSheet: CartTokoFoodBottomSheet = CartTokoFoodBottomSheet()
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
    fun getMetadata(): CartMetadataTokoFoodWithVariant? {
        return Gson().fromJson(metadata, CartMetadataTokoFoodWithVariant::class.java)
    }

}

data class CartTokoFoodBottomSheet(
    @SerializedName("is_show_bottomsheet")
    @Expose
    val isShowBottomSheet: Boolean = false,
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("buttons")
    @Expose
    val buttons: List<CartTokoFoodBottomSheetButton> = listOf()
)

data class CartTokoFoodBottomSheetButton(
    @SerializedName("text")
    @Expose
    val text: String = "",
    @SerializedName("color")
    @Expose
    val color: String = "",
    @SerializedName("action")
    @Expose
    val action: Int = 0,
    @SerializedName("link")
    @Expose
    val link: String = ""
)