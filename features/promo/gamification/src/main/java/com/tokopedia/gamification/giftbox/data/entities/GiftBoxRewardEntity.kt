package com.tokopedia.gamification.giftbox.data.entities

import com.google.gson.annotations.SerializedName

class GiftBoxRewardEntity(@SerializedName("gamiCrack") val gamiCrack: GamiCrack) {
    var couponDetailResponse: CouponDetailResponse? = null
}

data class GamiCrack(

        @SerializedName("resultStatus") val resultStatus: ResultStatus,
        @SerializedName("imageUrl") val imageUrl: String,
        @SerializedName("benefitText") val benefitText: List<String>,
        @SerializedName("benefits") val benefits: List<Benefits>?,
        @SerializedName("actionButton") val actionButton: List<ActionButton>,
        @SerializedName("recommendation") val recommendation: Recommendation
)
data class Recommendation(
        @SerializedName("isShow") val isShow: Boolean?,
        @SerializedName("shopID") val shopId: String?,
        @SerializedName("pageName") val pageName: String?,
)
data class Benefits(

        @SerializedName("text") val text: String,
        @SerializedName("color") val color: String?,
        @SerializedName("imageURL") val imageUrl: String?,
        @SerializedName("benefitType") val benefitType: String?,
        @SerializedName("displayType") val displayType: String?,
        @SerializedName("isBigPrize") val isBigPrize: Boolean,
        @SerializedName("isAutoApply") val isAutoApply: Boolean,
        @SerializedName("autoApplyMsg") val autoApplyMsg: String,
        @SerializedName("dummyCode") val dummyCode: String,
        @SerializedName("referenceID") val referenceID: String?
)