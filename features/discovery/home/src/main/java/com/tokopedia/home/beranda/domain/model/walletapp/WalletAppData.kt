package com.tokopedia.home.beranda.domain.model.walletapp


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletAppData(
    @Expose
    @SerializedName("walletappGetBalances")
    val walletappGetBalance: WalletappGetBalance = WalletappGetBalance()
)