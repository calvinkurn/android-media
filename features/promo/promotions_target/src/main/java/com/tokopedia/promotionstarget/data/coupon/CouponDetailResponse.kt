package com.tokopedia.promotionstarget.data.coupon

import com.google.gson.annotations.SerializedName

data class GetCouponDetailResponse(val couponList: ArrayList<GetCouponDetail>?)
data class GetCouponDetail(
        @SerializedName("id") val id: Int,
        @SerializedName("minimumUsageLabel") val minimumUsageLabel: String?,
        @SerializedName("minimumUsage") val minimumUsage: String?,
        @SerializedName("icon") val icon: String?,
        @SerializedName("image_url_mobile") val imageUrl: String?):CouponUiData()

data class TokopointsCouponDetail(
        @SerializedName("id") val id: Int,
        @SerializedName("minimumUsageLabel") val minimumUsageLabel: String?,
        @SerializedName("minimumUsage") val minimumUsage: String?,
        @SerializedName("icon") val icon: String?,
        @SerializedName("statusStr") val statusStr: String?,
        @CouponStatusType @SerializedName("status") val couponStatus: Int?,
        @SerializedName("image_url_mobile") val imageUrl: String?):CouponUiData()

sealed class CouponUiData