package com.tokopedia.settingbank.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckAccountResponse(
        @SerializedName("CheckAccountNumber")
        @Expose
        val checkAccountData: CheckAccountData

)

data class CheckAccountData(
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
        val accountHolderName: String? = null,
        @SerializedName("isValidBankAccount")
        @Expose
        val isValidBankAccount: Boolean,
        @SerializedName("allowedToEdit")
        @Expose
        val allowedToEdit: Boolean

)
