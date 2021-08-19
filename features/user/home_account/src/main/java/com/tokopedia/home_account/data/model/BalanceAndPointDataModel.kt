package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BalanceAndPointDataModel {
    @SerializedName("walletappGetAccountBalance") @Expose
    val data: WalletappGetAccountBalance = WalletappGetAccountBalance()
}

data class WalletappGetAccountBalance(
    @SerializedName("id") @Expose
    val id: String = "",
    @SerializedName("title") @Expose
    val title: String = "",
    @SerializedName("subtitle") @Expose
    val subtitle: String = "",
    @SerializedName("subtitle_color") @Expose
    val subtitleColor: String = "",
    @SerializedName("applink") @Expose
    val applink: String = "",
    @SerializedName("weblink") @Expose
    val weblink: String = "",
    @SerializedName("icon") @Expose
    val icon: String = "",
    @SerializedName("is_active") @Expose
    val isActive: Boolean = false
)