package com.tokopedia.home.beranda.domain.model.walletapp


import com.google.gson.annotations.SerializedName

data class WalletAppData(
    @SerializedName("walletappGetBalances")
    val walletappGetBalance: WalletappGetBalance = WalletappGetBalance()
)