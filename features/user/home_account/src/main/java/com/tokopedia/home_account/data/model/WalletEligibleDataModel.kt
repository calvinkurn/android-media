package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WalletEligibleDataModel {
    @SerializedName("walletappGetWalletEligible") @Expose
    val data: WalletappWalletEligibility = WalletappWalletEligibility()
}

data class WalletappWalletEligibility(
    @SerializedName("code") @Expose
    val code: String = "",
    @SerializedName("message") @Expose
    val message: String = "",
    @SerializedName("data") @Expose
    val data: MutableList<WalletEligibility> = mutableListOf()
)

data class WalletEligibility(
    @SerializedName("wallet_code") @Expose
    val walletCode: String = "",
    @SerializedName("is_eligible") @Expose
    val isEligible: Boolean = false
)