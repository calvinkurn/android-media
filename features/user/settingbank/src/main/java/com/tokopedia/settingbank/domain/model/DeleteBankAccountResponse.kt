package com.tokopedia.settingbank.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DeleteBankAccountResponse(
        @SerializedName("DeleteBankAccount")
        @Expose
        val deleteBankAccount: DeleteBankAccount
)

data class DeleteBankAccount(
        @SerializedName("status")
        @Expose
        val status: String?,
        @SerializedName("header")
        @Expose
        val header: DeleteBankAccountHeader?,
        @SerializedName("data")
        @Expose
        val data: DeleteBankAccountData?
)

data class DeleteBankAccountData(
        @SerializedName("success")
        @Expose
        val isSuccess: Boolean,
        @SerializedName("message")
        @Expose
        val messages: String?
)

data class DeleteBankAccountHeader(
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
