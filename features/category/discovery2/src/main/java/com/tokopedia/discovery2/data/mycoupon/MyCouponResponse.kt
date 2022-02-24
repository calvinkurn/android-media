package com.tokopedia.discovery2.data.mycoupon

import com.google.gson.annotations.SerializedName

data class MyCouponResponse(
        @SerializedName("tokopointsCouponListStack")
        val tokopointsCouponListStack: TokopointsCouponListStack? = null
)

data class TokopointsCouponListStack(
        @SerializedName("coupons")
        var coupons: List<MyCoupon>? = null,
        @SerializedName("tokopointsPaging")
        val tokopointsPaging: TokopointsPaging? = null,
        @SerializedName("redeemMessage")
        val redeemMessage: String? = "",
)

data class MyCoupon(
        @SerializedName("catalogID")
        val catalogID: Int? = null,
        @SerializedName("title")
        val title: String? = "",
        @SerializedName("code")
        val code: String? = "",
        @SerializedName("imageURL")
        val imageURL: String? = "",
        @SerializedName("imageURLMobile")
        val imageURLMobile: String? = "",
        @SerializedName("minimumUsage")
        val minimumUsage: String? = "",
        @SerializedName("minimumUsageLabel")
        val minimumUsageLabel: String? = "",
        @SerializedName("isNewCoupon")
        val isNewCoupon: Boolean? = null,
        @SerializedName("isStacked")
        val isStacked: Boolean? = null,
        @SerializedName("stackID")
        val stackID: String? = "",
        @SerializedName("isHighlighted")
        val isHighlighted: Boolean? = null,
        @SerializedName("upperLeftSection")
        val upperLeftSection: UpperLeftSection? = null,
        @SerializedName("promoInfo")
        val promoInfo: PromoInfo? = null,
)

data class PromoInfo(
        @SerializedName("promoType")
        val promoType: Int? = null,
        @SerializedName("promoIcon")
        val promoIcon: String? = null
)

data class UpperLeftSection(
        @SerializedName("textAttributes")
        val textAttributes: List<String?>? = null,
        @SerializedName("backgroundColor")
        val backgroundColor: String? = ""
)

data class TokopointsPaging(
        @SerializedName("hasNext")
        val hasNext: Boolean? = null
)