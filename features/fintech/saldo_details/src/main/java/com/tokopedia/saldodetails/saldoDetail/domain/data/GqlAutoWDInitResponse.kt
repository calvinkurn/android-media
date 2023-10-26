package com.tokopedia.saldodetails.saldoDetail.domain.data

import com.google.gson.annotations.SerializedName

data class GqlAutoWDInitResponse(
    @SerializedName("RichieAutoWDInit")
    val richieAutoWDInit: RichieAutoWDInit = RichieAutoWDInit()
)

data class RichieAutoWDInit(
    @SerializedName("code")
    val code: String = "",

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: AutoWDInitData = AutoWDInitData()
) {
    fun isSuccess(): Boolean = code == SUCCESS

    companion object {
        private const val SUCCESS = "SUCCESS"
    }
}

data class AutoWDInitData(
    @SerializedName("is_eligible")
    val isEligible: Boolean = true,

    @SerializedName("status")
    val status: Int = 0,

    @SerializedName("schedule_wording")
    val scheduleWording: String = "",

    @SerializedName("redirect_link")
    val redirectLink: String = ""
)
