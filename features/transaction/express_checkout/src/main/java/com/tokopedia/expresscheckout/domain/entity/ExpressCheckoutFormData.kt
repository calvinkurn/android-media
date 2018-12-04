package com.tokopedia.expresscheckout.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.transactiondata.entity.response.cartlist.AutoApply
import com.tokopedia.transactiondata.entity.response.cartlist.PromoSuggestion
import com.tokopedia.transactiondata.entity.response.shippingaddressform.Donation

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class ExpressCheckoutFormData(

        @SerializedName("errors")
        @Expose
        val errors: ArrayList<String>,

        @SerializedName("error_code")
        @Expose
        val errorCode: Int,

        @SerializedName("success")
        @Expose
        val success: Int,

        @SerializedName("cart")
        @Expose
        val cart: Cart,

        @SerializedName("is_coupon_active")
        @Expose
        val isCouponActive: Int,

        @SerializedName("kero_token")
        @Expose
        val keroToken: String,

        @SerializedName("kero_discom_token")
        @Expose
        val keroDiscomToken: String,

        @SerializedName("kero_unix_time")
        @Expose
        val keroUnixTime: Long,

        @SerializedName("enable_partial_cancel")
        @Expose
        val enablePartialCancel: Boolean,

        @SerializedName("donation")
        @Expose
        val donation: Donation?,

        @SerializedName("promo_suggestion")
        @Expose
        val promoSuggestion: PromoSuggestion?,

        @SerializedName("autoapply")
        @Expose
        val autoapply: AutoApply?,

        @SerializedName("user_profile_default")
        @Expose
        val userProfileDefault: UserProfile?

)