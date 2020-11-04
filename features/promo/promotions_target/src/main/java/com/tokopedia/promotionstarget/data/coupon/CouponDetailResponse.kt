package com.tokopedia.promotionstarget.data.coupon

import com.google.gson.annotations.SerializedName

data class GetCouponDetailResponse(val couponList: ArrayList<GetCouponDetail>?)
data class GetCouponDetail(
        @SerializedName("id") val id: Int,
        @SerializedName("minimumUsageLabel") val minimumUsageLabel: String?,
        @SerializedName("minimumUsage") val minimumUsage: String?,
        @SerializedName("icon") val icon: String?,
        @SerializedName("image_url_mobile") val imageUrl: String?) : CouponUiData()

data class TokopointsCouponDetail(
        @SerializedName("id") val id: Int,
        @SerializedName("minimum_usage_label") val minimumUsageLabel: String?,
        @SerializedName("minimum_usage") val minimumUsage: String?,
        @SerializedName("statusStr") val statusStr: String?,
        @SerializedName("is_applicable") val isApplicable: Boolean?,
        @SerializedName("real_code") val realCode: String?,
        @SerializedName("usage") val usage: Usage?,
        @CouponStatusType @SerializedName("status") val couponStatus: Int?,
        @SerializedName("thumbnail_url_mobile") val imageUrl: String?) : CouponUiData()

data class Usage(
        @SerializedName("text") val dateKey: String?,
        @SerializedName("usage_str") val dateValue: String?,
        @SerializedName("btn_usage") val btnUsage: ButtonUsage?
)
data class ButtonUsage(
        @SerializedName("applink") val applink: String?,
        @SerializedName("text") val text: String?
)

sealed class CouponUiData