package com.tokopedia.thankyou_native.domain.model

import com.google.gson.annotations.SerializedName

data class WalletBalanceGqlResponse(
    @SerializedName("walletappGetBalance")
    val walletBalance: WalletBalance
)

data class WalletBalance(
    @SerializedName("code")
    val code: String,
    @SerializedName("balance")
    val balanceList: ArrayList<WalletBalanceItem>,
)

data class WalletBalanceItem(
    @SerializedName("wallet_code")
    val walletCode: String,
    @SerializedName("amount")
    val walletAmount: String,
    @SerializedName("active")
    val isActive: Boolean,
    @SerializedName("whitelisted")
    val whitelisted: Boolean?
)
