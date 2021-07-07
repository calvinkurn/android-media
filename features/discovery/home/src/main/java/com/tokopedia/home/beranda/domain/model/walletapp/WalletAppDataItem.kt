package com.tokopedia.home.beranda.domain.model.walletapp


import com.google.gson.annotations.SerializedName

data class WalletAppDataItem(
    @SerializedName("data")
    val `data`: WalletAppData = WalletAppData()
)