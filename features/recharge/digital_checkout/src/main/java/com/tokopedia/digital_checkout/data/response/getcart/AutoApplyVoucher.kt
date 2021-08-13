package com.tokopedia.digital_checkout.data.response.getcart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 08/01/21
 */

data class AutoApplyVoucher(
        @SerializedName("success")
        @Expose
        val success: Boolean = false,

        @SerializedName("message")
        @Expose
        val message: Message = Message(),

        @SerializedName("code")
        @Expose
        val code: String = "",

        @SerializedName("is_coupon")
        @Expose
        val isCoupon: Int = 0,

        @SerializedName("discount_amount")
        @Expose
        val discountAmount: Double = 0.0,

        @SerializedName("discount_price")
        @Expose
        val discountPrice: String = "",

        @SerializedName("discounted_amount")
        @Expose
        val discountedAmount: Double = 0.0,

        @SerializedName("discounted_price")
        @Expose
        val discountedPrice: String = "",

        @SerializedName("title_description")
        @Expose
        val titleDescription: String = "",

        @SerializedName("promo_code_id")
        @Expose
        val promoCodeId: String = "0",

        @SerializedName("promo_id")
        @Expose
        val promoId: String = "",

        @SerializedName("message_success")
        @Expose
        val messageSuccess: String = ""
) {
    data class Message(
            @SerializedName("state")
            @Expose
            val state: String = "",

            @SerializedName("color")
            @Expose
            val color: String = "",

            @SerializedName("text")
            @Expose
            val text: String = ""
    )
}