package com.tokopedia.navigation_common.usecase.pojo.walletapp


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletAppDataItem(
    @Expose
    @SerializedName("data")
    val `data`: WalletAppData = WalletAppData()
)