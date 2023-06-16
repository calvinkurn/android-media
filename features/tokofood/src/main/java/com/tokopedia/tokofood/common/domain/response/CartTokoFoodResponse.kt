package com.tokopedia.tokofood.common.domain.response

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFoodWithVariant
import kotlinx.parcelize.Parcelize

data class AddToCartTokoFoodResponse(
    @SerializedName("add_to_cart_general")
    val cartResponse: CartTokoFoodResponse
)

data class RemoveCartTokoFoodResponse(
    @SerializedName("remove_cart_general")
    val cartResponse: CartTokoFoodResponse
)

data class UpdateCartTokoFoodResponse(
    @SerializedName("update_cart_general")
    val cartResponse: CartTokoFoodResponse
)

data class CartTokoFoodResponse(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
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
    val success: Int = 0,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("carts")
    val carts: List<CartTokoFood> = listOf(),
    @SerializedName("bottomsheet")
    val bottomSheet: CartTokoFoodBottomSheet = CartTokoFoodBottomSheet()
)

data class CartTokoFood(
    @SerializedName("success")
    val success: Int = 0,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("business_id")
    val businessId: String = "",
    @SerializedName("cart_id")
    val cartId: String = "",
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("shop_id")
    val shopId: String = "",
    @SerializedName("quantity")
    val quantity: Int = 0,
    @SerializedName("metadata")
    val metadata: String = ""
) {
    fun getMetadata(): CartMetadataTokoFoodWithVariant? {
        return try {
            Gson().fromJson(metadata, CartMetadataTokoFoodWithVariant::class.java)
        } catch (ex: Exception) {
            null
        }
    }

}

@Parcelize
data class CartTokoFoodBottomSheet(
    @SerializedName("is_show_bottomsheet")
    val isShowBottomSheet: Boolean = false,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("buttons")
    val buttons: List<CartTokoFoodBottomSheetButton> = listOf()
) : Parcelable

@Parcelize
data class CartTokoFoodBottomSheetButton(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("color")
    val color: String = "",
    @SerializedName("action")
    val action: Int = 0,
    @SerializedName("link")
    val link: String = ""
) : Parcelable
