package com.tokopedia.gamification.giftbox.data.entities

import com.google.gson.annotations.SerializedName

data class CouponDetailResponse(val couponMap: HashMap<String, GetCouponDetail>?)
data class GetCouponDetail(
        @SerializedName("minimumUsageLabel") val minimumUsageLabel: String?,
        @SerializedName("minimumUsage") val minimumUsage: String?,
        @SerializedName("icon") val icon: String?,
        @SerializedName("image_url_mobile") val imageUrl: String?) : CouponType

data class CouponTapTap(val imageUrl: String?) : CouponType
data class OvoListItem(val imageUrl: String?, val text:String) : CouponType

interface CouponType