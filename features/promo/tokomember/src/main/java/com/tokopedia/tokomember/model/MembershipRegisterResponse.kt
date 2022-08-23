package com.tokopedia.tokomember.model

import com.google.gson.annotations.SerializedName

data class MembershipRegisterResponse(
    @SerializedName("membershipRegister") val data: MembershipRegister? = null
)

data class MembershipRegister(
    @SerializedName("resultStatus") val resultStatus: ResultStatus?,
    @SerializedName("infoMessage") val infoMessage: InfoMessage?,
)

data class InfoMessage(
    @SerializedName("imageURL") val imageURL: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("subtitle") val subtitle: String?,
    @SerializedName("cta") val cta: Cta?,
)

data class Cta(
    @SerializedName("text") val text: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("appLink") val appLink: String?
)

data class ResultStatus(
    @SerializedName("code") val code: String?,
    @SerializedName("message") val message: List<String?>?,
    @SerializedName("status") val status: String?,
    @SerializedName("reason") val reason: String?
)