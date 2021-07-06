package com.tokopedia.home.beranda.domain.model.walletapp


import com.google.gson.annotations.SerializedName

data class Balance(
    @SerializedName("active")
    val active: Boolean = false,
    @SerializedName("amount")
    val amount: Int = 0,
    @SerializedName("amount_fmt")
    val amountFmt: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("wallet_code")
    val walletCode: String = ""
)