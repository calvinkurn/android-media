package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil

data class AgreeConsentResponse(
    @SerializedName("data")
    @Expose
    val data: AgreeConsent = AgreeConsent()
)

data class AgreeConsent(
    @SerializedName("tokofoodSubmitUserConsent")
    @Expose
    val tokofoodSubmitUserConsent: AgreeConsentData = AgreeConsentData()
)

data class AgreeConsentData(
    @SerializedName("success")
    @Expose
    val isSuccess: Boolean = false,
    @SerializedName("message")
    @Expose
    val message: String = ""
)