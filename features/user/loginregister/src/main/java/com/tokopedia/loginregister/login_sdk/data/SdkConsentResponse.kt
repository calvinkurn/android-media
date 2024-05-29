package com.tokopedia.loginregister.login_sdk.data

import com.google.gson.annotations.SerializedName

data class SdkConsentResponse (
    @SerializedName("oauth_check_consent")
    val data: SdkConsentData
)

data class SdkConsentData(
    @SerializedName("show_consent")
    val showConsent: Boolean,
    @SerializedName("user_info")
    val userInfo: UserInfo,
    @SerializedName("client_info")
    val clientInfo: ClientInfo,
    @SerializedName("term_and_privacy_url")
    val termPrivacy: TermPrivacy,
    @SerializedName("consent")
    var consents: List<String> = listOf(),
    @SerializedName("is_success")
    val isSuccess: Boolean,
    @SerializedName("error")
    val error: String
)

data class UserInfo(
    @SerializedName("fullname")
    val fullName: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("phone_number")
    val phone: String = "",
    @SerializedName("profile_picture")
    val profilePicture: String,
)


data class ClientInfo(
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("app_name")
    val appName: String = "",
    @SerializedName("redirect_uri")
    val redirectUri: String = "",
    @SerializedName("is_external")
    val isExternal: Boolean
)

data class TermPrivacy(
    @SerializedName("tokopedia_tnc_id")
    val tncId: String = "",
    @SerializedName("tnc_url")
    val url: String = "",
    @SerializedName("privacy_url")
    val privacyUrl: String = "",
    @SerializedName("purpose")
    val purpose: String = ""
)
