package com.tokopedia.withdraw.saldowithdrawal.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GqlTopadsAutoTopupWithdrawRecomResponse(
    @SerializedName("topAdsAutoTopupWithdrawalRecom")
    @Expose
    val topAdsAutoTopupWithdrawalRecom: TopAdsAutoTopupWithdrawalRecom = TopAdsAutoTopupWithdrawalRecom()
)

data class TopAdsAutoTopupWithdrawalRecom(
    @SerializedName("data")
    @Expose
    val data: TopadsAutoTopupWithdrawRecomData = TopadsAutoTopupWithdrawRecomData()
)

data class TopadsAutoTopupWithdrawRecomData(
    @SerializedName("auto_topup_status")
    @Expose
    val autoTopUpStatus: Int = 0,

    @SerializedName("recommendation_value")
    @Expose
    val recommendationValue: Double = 0.0
)
