package com.tokopedia.revamp.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetWarningDataModel(
    @Expose @SerializedName("is_success")
    private var isSuccess: Int = 0,
    @Expose @SerializedName("warning")
    private var warning: MutableList<String> = mutableListOf(),
    @Expose @SerializedName("saldo")
    private var saldo: String = "",
    @Expose @SerializedName("tokocash")
    private var tokocash: String = "",
    @Expose @SerializedName("saldo_number")
    private var saldoNumber: Long = 0,
    @Expose @SerializedName("tokocash_number")
    private var tokocashNumber: Long = 0,
    @Expose @SerializedName("action")
    private var action: String = "",
    @Expose @SerializedName("have_bank_acct")
    private var hasBankAccount: Boolean = false,
    @Expose @SerializedName("is_ovo_eligible")
    private var isOvoEligible: Boolean = false,
    @Expose @SerializedName("is_ovo_primary")
    private var isOvoPrimary: Boolean = false,
    @Expose @SerializedName("is_ovo_activated")
    private var isOvoActivated: Boolean = false,
    @Expose @SerializedName("redirect_url")
    private var redirectUrl: String = ""
)