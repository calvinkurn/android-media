package com.tokopedia.oneclickcheckout.order.data.update

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.localizationchooseaddress.common.ChosenAddress

data class UpdateCartOccRequest(
        @SerializedName("cart")
        val cart: ArrayList<UpdateCartOccCartRequest> = ArrayList(),
        @SerializedName("profile")
        val profile: UpdateCartOccProfileRequest = UpdateCartOccProfileRequest(),
        @SerializedName("chosen_address")
        var chosenAddress: ChosenAddress? = null,
        @SerializedName("skip_shipping_validation")
        val skipShippingValidation: Boolean = false,
        @SerializedName("source")
        var source: String = ""
) {
        companion object {
                const val SOURCE_UPDATE_QTY_NOTES = "update_qty_notes"
                const val SOURCE_UPDATE_OCC_ADDRESS = "update_occ_address"
                const val SOURCE_UPDATE_OCC_PAYMENT = "update_occ_payment"
        }
}

data class UpdateCartOccCartRequest(
        @SerializedName("cart_id")
        val cartId: String? = null,
        @SerializedName("quantity")
        val quantity: Int = 1,
        @SerializedName("notes")
        val notes: String = "",
        @SerializedName("product_id")
        val productId: String = ""
)

data class UpdateCartOccProfileRequest(
        @SerializedName("gateway_code")
        val gatewayCode: String = "",
        @SerializedName("metadata")
        val metadata: String = "",
        @SerializedName("address_id")
        val addressId: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("service_id")
        val serviceId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("shipping_id")
        val shippingId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("sp_id")
        val spId: Int = 0,
        @SerializedName("is_free_shipping_selected")
        val isFreeShippingSelected: Boolean = false,
        @SerializedName("tenure_type")
        val tenureType: Int = 0,
        @SerializedName("option_id")
        val optionId: String = "",
) {
    companion object {
        const val EXPRESS_CHECKOUT_PARAM = "express_checkout_param"
        const val INSTALLMENT_TERM = "installment_term"
    }
}