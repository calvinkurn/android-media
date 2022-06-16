package com.tokopedia.tokofood.common.domain.response

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFood
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFoodWithVariant
import kotlinx.parcelize.Parcelize

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
    @Expose
    val isShowBottomSheet: Boolean = false,
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("image_url")
    @Expose
    val imageUrl: String = "",
    @SerializedName("buttons")
    @Expose
    val buttons: List<CartTokoFoodBottomSheetButton> = listOf()
) : Parcelable

@Parcelize
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
) : Parcelable