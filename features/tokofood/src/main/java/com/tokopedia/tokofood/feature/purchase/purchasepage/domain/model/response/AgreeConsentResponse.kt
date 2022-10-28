package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response

import com.google.gson.annotations.SerializedName

data class AgreeConsentResponse(
    @SerializedName("tokofoodSubmitUserConsent")
    val tokofoodSubmitUserConsent: AgreeConsentData = AgreeConsentData()
)

data class AgreeConsentData(
    @SerializedName("success")
    val isSuccess: Boolean = false,
    @SerializedName("message")
    val message: String = ""
)