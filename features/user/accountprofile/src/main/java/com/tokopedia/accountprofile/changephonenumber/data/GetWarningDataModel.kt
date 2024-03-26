package com.tokopedia.accountprofile.changephonenumber.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetWarningDataModel(
    @Expose @SerializedName("is_success")
    var isSuccess: Int = 0,
    @Expose @SerializedName("warning")
    var warning: MutableList<String> = mutableListOf(),
    @Expose @SerializedName("saldo")
    var saldo: String = "",
    @Expose @SerializedName("tokocash")
    var tokocash: String = "",
    @Expose @SerializedName("saldo_number")
    var saldoNumber: Long = 0,
    @Expose @SerializedName("tokocash_number")
    var tokocashNumber: Long = 0,
    @Expose @SerializedName("action")
    var action: String = "",
    @Expose @SerializedName("have_bank_acct")
    var hasBankAccount: Boolean = false,
    @Expose @SerializedName("is_ovo_eligible")
    var isOvoEligible: Boolean = false,
    @Expose @SerializedName("is_ovo_primary")
    var isOvoPrimary: Boolean = false,
    @Expose @SerializedName("is_ovo_activated")
    var isOvoActivated: Boolean = false,
    @Expose @SerializedName("redirect_url")
    var redirectUrl: String = ""
)
