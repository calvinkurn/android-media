package com.tokopedia.settingbank.banklist.v2.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BankAccountListResponse(
        @SerializedName("GetBankAccount")
        @Expose
        val getBankAccount: GetBankAccount
)

data class GetBankAccount(
        @SerializedName("status")
        @Expose
        val status: String? = null,
        @SerializedName("header")
        @Expose
        val header: Header,
        @SerializedName("data")
        @Expose
        val data: Data
)

data class Header(
        @SerializedName("processTime")
        @Expose
        val processTime: Double,
        @SerializedName("message")
        @Expose
        val message: List<String>? = null,
        @SerializedName("reason")
        @Expose
        val reason: String? = null,
        @SerializedName("errorCode")
        @Expose
        val errorCode: String? = null
)

data class Data(
        @SerializedName("bankAccounts")
        @Expose
        val bankAccount: List<BankAccount>? = null,
        @SerializedName("userInfo")
        @Expose
        val userInfo: UserInfo
)

data class BankAccount(
        @SerializedName("accID")
        @Expose
        val accID: Long,
        @SerializedName("accName")
        @Expose
        val accName: String? = null,
        @SerializedName("accNumber")
        @Expose
        val accNumber: String? = null,
        @SerializedName("bankID")
        @Expose
        val bankID: Long,
        @SerializedName("bankName")
        @Expose
        val bankName: String? = null,
        @SerializedName("fsp")
        @Expose
        val fsp: Long,
        @SerializedName("bankImageUrl")
        @Expose
        val bankImageUrl: String? = null,
        @SerializedName("statusFraud")
        @Expose
        val statusFraud: Int,
        @SerializedName("copyWriting")
        @Expose
        val copyWriting: String?)

data class UserInfo(
        @SerializedName("message")
        @Expose
        val message: String? = null,
        @SerializedName("isVerified")
        @Expose
        val isVerified: Boolean
)

