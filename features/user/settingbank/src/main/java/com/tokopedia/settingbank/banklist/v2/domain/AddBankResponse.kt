package com.tokopedia.settingbank.banklist.v2.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
        @SerializedName("acc_id")
        @Expose
        val accountId: Long,
        @SerializedName("user_id")
        @Expose
        val userId: Long,
        @SerializedName("bank_id")
        @Expose
        val bankId: Long,
        @SerializedName("acc_no")
        @Expose
        val accountNumber: String,
        @SerializedName("acc_name")
        @Expose
        val accountName: String
)