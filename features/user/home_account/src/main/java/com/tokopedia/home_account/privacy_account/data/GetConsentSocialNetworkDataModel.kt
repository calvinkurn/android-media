package com.tokopedia.home_account.privacy_account.data

import com.google.gson.annotations.SerializedName

data class GetConsentDataModel(
    @SerializedName("SocialNetworkGetConsent")
    val socialNetworkGetConsent: SocialNetworkGetConsent = SocialNetworkGetConsent(),
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