package com.tokopedia.purchase_platform.express_checkout.data.entity.response.atc

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.express_checkout.data.entity.response.profile.Profile
import com.tokopedia.purchase_platform.common.feature.promo.data.model.response.AutoApply
import com.tokopedia.purchase_platform.common.feature.promo.data.model.response.PromoSuggestion
import com.tokopedia.purchase_platform.checkout.data.model.response.shipment_address_form.Donation

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class AtcData(

        @SerializedName("errors")
        val errors: ArrayList<String>,

        @SerializedName("error_code")
        val errorCode: Int,

        @SerializedName("success")
        val success: Int,

        @SerializedName("cart")
        val cart: Cart,

        @SerializedName("is_coupon_active")
        val isCouponActive: Int,

        @SerializedName("kero_token")
        val keroToken: String,

        @SerializedName("kero_discom_token")
        val keroDiscomToken: String,

        @SerializedName("kero_unix_time")
        val keroUnixTime: Long,

        @SerializedName("enable_partial_cancel")
        val enablePartialCancel: Boolean,

        @SerializedName("donation")
        val donation: Donation?,

        @SerializedName("promo_suggestion")
        val promoSuggestion: PromoSuggestion?,

        @SerializedName("autoapply")
        val autoapply: AutoApply?,

        @SerializedName("user_profile_default")
        val userProfileDefault: Profile?,

        @SerializedName("messages")
        val messages: ArrayList<Message>?,

        @SerializedName("max_quantity")
        val maxQuantity: Int?,

        @SerializedName("max_char_note")
        val maxCharNote: Int?

)