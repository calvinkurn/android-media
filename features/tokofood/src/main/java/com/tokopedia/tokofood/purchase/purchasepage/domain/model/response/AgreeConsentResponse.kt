package com.tokopedia.tokofood.purchase.purchasepage.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AgreeConsentResponse(
    @SerializedName("data")
    @Expose
    val data: AgreeConsentData = AgreeConsentData()
)

data class AgreeConsentData(
    @SerializedName("success")
    @Expose
    val success: Int = 0,
    @SerializedName("message")
    @Expose
    val message: String = ""
) {
    // TODO: Move to const
    fun isSuccess() = success == 1
}