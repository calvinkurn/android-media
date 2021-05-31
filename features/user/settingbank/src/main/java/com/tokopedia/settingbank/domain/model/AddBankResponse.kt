package com.tokopedia.settingbank.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RichieAddBankAccountNewFlow(
        @SerializedName("richieAddBankAccountNewFlow")
        @Expose
        val response: AddBankResponse
)

data class AddBankResponse(
        @SerializedName("status")
        @Expose
        val status: Int,
        @SerializedName("message")
        @Expose
        val message: String,
        @SerializedName("data")
        @Expose
        val data: AccountData
)

data class AccountData(
        @SerializedName("accID")
        @Expose
        val accountId: Long,
        @SerializedName("userID")
        @Expose
        val userId: Long,
        @SerializedName("bankID")
        @Expose
        val bankId: Long,
        @SerializedName("accNo")
        @Expose
        val accountNumber: String,
        @SerializedName("accName")
        @Expose
        val accountName: String
)