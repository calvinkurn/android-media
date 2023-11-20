package com.tokopedia.digital_product_detail.data.model.data

import com.google.gson.annotations.SerializedName

data class DigitalSaveAccessTokenResponse(
    @SerializedName("rechargeSaveTelcoUserBalanceAccessToken")
    val rechargeSaveTelcoUserBalanceAccessToken: RechargeSaveTelcoUserBalanceAccessToken
)

data class RechargeSaveTelcoUserBalanceAccessToken(
    @SerializedName("agentRC")
    val agentRC: String,
    @SerializedName("grc")
    val grc: String,
    @SerializedName("message")
    val message: String
)
