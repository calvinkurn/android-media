package com.tokopedia.gamification.giftbox.data.entities

import com.google.gson.annotations.SerializedName

data class GiftBoxRewardEntity(@SerializedName("crackResult") val crackResult: CrackResult)

data class CrackResult(

    @SerializedName("resultStatus") val resultStatus: ResultStatus,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("benefitText") val benefitText: List<String>,
    @SerializedName("benefits") val benefits: List<Benefits>,
    @SerializedName("actionButton") val actionButton: List<ActionButton>
)

data class Benefits(

    @SerializedName("text") val text: String,
    @SerializedName("color") val color: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("benefitType") val benefitType: String,
    @SerializedName("isBigPrize") val isBigPrize: Boolean,
    @SerializedName("isAutoApply") val isAutoApply: Boolean,
    @SerializedName("autoApplyMsg") val autoApplyMsg: String,
    @SerializedName("dummyCode") val dummyCode: String,
    @SerializedName("referenceID") val referenceID: Int
)