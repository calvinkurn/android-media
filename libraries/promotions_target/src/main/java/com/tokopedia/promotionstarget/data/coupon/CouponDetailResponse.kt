package com.tokopedia.promotionstarget.data.coupon

import com.google.gson.annotations.SerializedName

data class GetCouponDetailResponse(val map: HashMap<String, GetCouponDetail>)
data class GetCouponDetail(
        @SerializedName("id") val id: Int,
        @SerializedName("title") val title: String)