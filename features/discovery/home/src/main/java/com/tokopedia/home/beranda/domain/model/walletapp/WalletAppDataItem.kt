package com.tokopedia.home.beranda.domain.model.walletapp


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletAppDataItem(
    @Expose
    @SerializedName("data")
    val `data`: WalletAppData = WalletAppData()
)