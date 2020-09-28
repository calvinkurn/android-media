package com.tokopedia.discovery2.data.quickcouponresponse

import com.google.gson.annotations.SerializedName

data class ClickCouponData(
        @SerializedName("coupon_applied")
        var couponApplied: Boolean? = false,
        @SerializedName("is_applicable")
        val isApplicable: Boolean? = false,
        @SerializedName("code_promo")
        val codePromo: String? = "",
        @SerializedName("real_code")
        val realCode: String? = "",
        @SerializedName("catalog_title")
        val catalogTitle: String? = "",
        @SerializedName("coupon_app_link")
        val couponAppLink: String? = "",
        @SerializedName("message_using_success")
        val messageUsingSuccess: String? = "",
        @SerializedName("message_using_failed")
        val messageUsingFailed: String? = "",

        var componentID: String = "",
        var componentPosition: Int = 0,
        var componentName: String? = ""

)