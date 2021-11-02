package com.tokopedia.navigation_common.usecase.pojo.walletapp


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletAppData(
    @Expose
    @SerializedName("walletappGetBalances")
    val walletappGetBalance: WalletappGetBalance = WalletappGetBalance()
)