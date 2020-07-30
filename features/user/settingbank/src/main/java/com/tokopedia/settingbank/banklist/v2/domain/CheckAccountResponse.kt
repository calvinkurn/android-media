package com.tokopedia.settingbank.banklist.v2.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckAccountResponse(
        @SerializedName("CheckAccountNumber")
        @Expose
        val checkAccountNumber: CheckAccountNumber

)

data class CheckAccountNumber(
        @SerializedName("successCode")
        @Expose
        val successCode: Int,

        @SerializedName("message")
        @Expose
        val message: String? = null,
        @SerializedName("bankID")
        @Expose
        val bankID: Long,
        @SerializedName("accountNumber")
        @Expose
        val accountNumber: String? = null,
        @SerializedName("bankName")
        @Expose
        val bankName: String? = null,
        @SerializedName("accountName")
        @Expose
        val accountHolderName: String? = null

)
