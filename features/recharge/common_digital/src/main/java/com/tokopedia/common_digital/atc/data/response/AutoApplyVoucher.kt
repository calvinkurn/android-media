package com.tokopedia.common_digital.atc.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AutoApplyVoucher (
        @SerializedName("success")
    @Expose
    var isSuccess: Boolean = false,

        @SerializedName("code")
    @Expose
    var code: String? = null,

        @SerializedName("is_coupon")
    @Expose
    var isCoupon: Int = 0,

        @SerializedName("discount_amount")
    @Expose
    var discountAmount: Double = 0.0,

        @SerializedName("title_description")
    @Expose
    var title: String? = null,

        @SerializedName("message_success")
    @Expose
    var messageSuccess: String? = null,

        @SerializedName("promo_id")
        @Expose
        var promoId: String = "0"
)
