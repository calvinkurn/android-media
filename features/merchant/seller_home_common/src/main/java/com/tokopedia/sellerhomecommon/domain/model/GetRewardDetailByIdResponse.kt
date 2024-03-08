package com.tokopedia.sellerhomecommon.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

data class GetRewardDetailByIdResponse(
    @SuppressLint("Invalid Data Type")
    @SerializedName("getRewardDetailByID")
    val data: GetRewardDetailByIdData = GetRewardDetailByIdData()
)

data class GetRewardDetailByIdData(
    @SerializedName("result")
    val result: GetRewardDetailByIdResult = GetRewardDetailByIdResult(),
    @SerializedName("error")
    val error: GetRewardDetailByIdError = GetRewardDetailByIdError()
)

data class GetRewardDetailByIdResult(
    @SerializedName("rewardID")
    val rewardId: String = String.EMPTY,
    @SerializedName("rewardTitle")
    val rewardTitle: String = String.EMPTY,
    @SerializedName("rewardSubtitle")
    val rewardSubtitle: String = String.EMPTY,
    @SerializedName("benefitList")
    val benefitList: List<GetRewardDetailByIdBenefit> = listOf(),
    @SerializedName("isUnlimited")
    val isUnlimited: Boolean = false,
    @SerializedName("rewardEstimationPrice")
    val rewardEstimationPrice: String = String.EMPTY,
    @SerializedName("rewardImage")
    val rewardImage: String = String.EMPTY,
    @SerializedName("metadata")
    val metadata: String = String.EMPTY
)

data class GetRewardDetailByIdError(
    @SerializedName("message")
    val message: String = String.EMPTY,
    @SerializedName("code")
    val code: Int = Int.ZERO
)

data class GetRewardDetailByIdBenefit(
    @SerializedName("benefitName")
    val benefitName: String = String.EMPTY,
    @SerializedName("benefitValue")
    val benefitValue: String = String.EMPTY
)
