package com.tokopedia.home.beranda.domain.model.walletapp


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletappGetBalance(
    @Expose
    @SerializedName("balances")
    val balances: List<Balances> = listOf()
)