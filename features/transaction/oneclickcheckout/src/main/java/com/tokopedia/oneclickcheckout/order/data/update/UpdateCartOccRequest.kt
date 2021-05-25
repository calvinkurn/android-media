package com.tokopedia.oneclickcheckout.order.data.update

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.localizationchooseaddress.util.ChosenAddress

data class UpdateCartOccRequest(
        @SerializedName("cart")
        val cart: ArrayList<UpdateCartOccCartRequest> = ArrayList(),
        @SerializedName("profile")
        val profile: UpdateCartOccProfileRequest = UpdateCartOccProfileRequest(),
        @SerializedName("chosen_address")
        var chosenAddress: ChosenAddress? = null,
        @SerializedName("skip_shipping_validation")
        val skipShippingValidation: Boolean = false
)

data class UpdateCartOccCartRequest(
        @SerializedName("cart_id")
        val cartId: String = "",
        @SerializedName("quantity")
        val quantity: Int = 1,
        @SerializedName("notes")
        val notes: String = "",
        @SerializedName("product_id")
        val productId: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("shipping_id")
        val shippingId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("sp_id")
        val spId: Int = 0,
        @SerializedName("is_free_shipping_selected")
        val isFreeShippingSelected: Boolean = false
)

data class UpdateCartOccProfileRequest(
        @SerializedName("profile_id")
        val profileId: String = "",
        @SerializedName("gateway_code")
        val gatewayCode: String = "",
        @SerializedName("metadata")
        val metadata: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("service_id")
        val serviceId: Int = 0,
        @SerializedName("address_id")
        val addressId: String = ""
) {
    companion object {
        const val EXPRESS_CHECKOUT_PARAM = "express_checkout_param"
        const val INSTALLMENT_TERM = "installment_term"
    }
}