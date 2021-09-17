package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BalanceAndPointDataModel {
    @SerializedName("walletappGetAccountBalance") @Expose
    var data: WalletappGetAccountBalance = WalletappGetAccountBalance()
}

data class WalletappGetAccountBalance(
    @SerializedName("id") @Expose
    var id: String = "",
    @SerializedName("title") @Expose
    var title: String = "",
    @SerializedName("subtitle") @Expose
    var subtitle: String = "",
    @SerializedName("subtitle_color") @Expose
    var subtitleColor: String = "",
    @SerializedName("applink") @Expose
    var applink: String = "",
    @SerializedName("weblink") @Expose
    var weblink: String = "",
    @SerializedName("icon") @Expose
    var icon: String = "",
    @SerializedName("is_active") @Expose
    var isActive: Boolean = false,
    @SerializedName("is_hidden") @Expose
    var isHidden: Boolean = false,
    @SerializedName("is_show") @Expose
    var isShow: Boolean = false
)