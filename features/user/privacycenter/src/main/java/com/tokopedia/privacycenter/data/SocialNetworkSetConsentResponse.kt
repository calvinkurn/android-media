package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class SocialNetworkSetConsentResponse(
    @SerializedName("SocialNetworkSetConsent")
    val socialNetworkSetConsent: SocialNetworkSetConsent = SocialNetworkSetConsent()
)

data class SocialNetworkSetConsent(
    @SerializedName("data")
    val data: DataSetConsent = DataSetConsent(),

    @SerializedName("messages")
    val messages: List<String> = emptyList(),

    @SerializedName("error_code")
    val errorCode: String = ""
)

data class DataSetConsent(
    @SerializedName("is_success")
    val isSuccess: Int = 0
)
