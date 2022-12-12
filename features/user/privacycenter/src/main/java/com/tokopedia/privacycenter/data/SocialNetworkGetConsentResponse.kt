package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class SocialNetworkGetConsentResponse(
    @SerializedName("SocialNetworkGetConsent")
    val socialNetworkGetConsent: SocialNetworkGetConsent = SocialNetworkGetConsent()
)

data class SocialNetworkGetConsent(
    @SerializedName("data")
    val data: DataUsage = DataUsage(),

    @SerializedName("messages")
    val messages: List<String> = emptyList(),

    @SerializedName("error_code")
    val errorCode: String = ""
)

data class DataUsage(
    @SerializedName("opt_in")
    val optIn: Boolean = false
)
