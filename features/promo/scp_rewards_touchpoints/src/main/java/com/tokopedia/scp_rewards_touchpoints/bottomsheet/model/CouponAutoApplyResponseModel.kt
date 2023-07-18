package com.tokopedia.scp_rewards_touchpoints.bottomsheet.model

import com.google.gson.annotations.SerializedName

data class CouponAutoApplyResponseModel(
    @SerializedName("scpRewardsCouponAutoApply") val data: ScpRewardsCouponAutoApply? = null
)

data class ScpRewardsCouponAutoApply(
    @SerializedName("resultStatus") val resultStatus: ResultStatus = ResultStatus(),
    @SerializedName("couponAutoApply") val couponAutoApply: CouponAutoApply? = null
) {
    data class ResultStatus(
        @SerializedName("code") val code: String = "",
        @SerializedName("status") val status: String = ""
    )
}

data class CouponAutoApply(
    @SerializedName("isSuccess") val isSuccess: Boolean = false,
    @SerializedName("infoMessage") val infoMessage: InfoMessage? = null
)

data class InfoMessage(
    @SerializedName("title") val title: String? = null,
    @SerializedName("subtitle") val subtitle: String? = null
)

fun CouponAutoApplyResponseModel.getMessage() = data?.couponAutoApply?.infoMessage?.title ?: ""

