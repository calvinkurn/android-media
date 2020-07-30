package com.tokopedia.settingbank.banklist.v2.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RichieSetPrimaryBankAccount(
        @SerializedName("richieSetPrimaryBankAccount")
        @Expose
        val richieSetPrimaryBankAccount: MakeAccountPrimaryResponse
)

data class MakeAccountPrimaryResponse(
        @SerializedName("status")
        @Expose
        val status: String?,
        @SerializedName("header")
        @Expose
        val header: MakeAccountPrimaryHeader?,
        @SerializedName("data")
        @Expose
        val data: MakeAccountPrimaryData?
)

data class MakeAccountPrimaryData(
        @SerializedName("isSuccess")
        @Expose
        val isSuccess: Boolean,
        @SerializedName("message")
        @Expose
        val messages: String?
)

data class MakeAccountPrimaryHeader(
        @SerializedName("message")
        @Expose
        val messageList: ArrayList<String>?,
        @SerializedName("reason")
        @Expose
        val reason: String?,
        @SerializedName("errorCode")
        @Expose
        val errorCode: String?
)
