package com.tokopedia.tokofood.purchase.purchasepage.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil

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

    fun isSuccess() = success == TokoFoodCartUtil.SUCCESS_STATUS

}