package com.tokopedia.v2.home.model.pojo.wallet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Wallet (
        @SerializedName("linked")
        @Expose
        val linked: Boolean = false,
        @SerializedName("balance")
        @Expose
        val balance: String = "",
        @SerializedName("rawBalance")
        @Expose
        val rawBalance: Int = 0,
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("total_balance")
        @Expose
        val totalBalance: String = "",
        @SerializedName("raw_total_balance")
        @Expose
        val rawTotalBalance: Int = 0,
        @SerializedName("hold_balance")
        @Expose
        val holdBalance: String = "",
        @SerializedName("raw_hold_balance")
        @Expose
        val rawHoldBalance: Int = 0,
        @SerializedName("redirect_url")
        @Expose
        val redirectUrl: String = "",
        @SerializedName("applinks")
        @Expose
        val applinks: String = "",
        @SerializedName("ab_tags")
        @Expose
        val abTags: List<AbTag> = listOf(),
        @SerializedName("action")
        @Expose
        val action: WalletAction = WalletAction(),
        @SerializedName("point_balance")
        @Expose
        val pointBalance: String = "",
        @SerializedName("raw_point_balance")
        @Expose
        val rawPointBalance: Int = 0,
        @SerializedName("cash_balance")
        @Expose
        val cashBalance: String = "",
        @SerializedName("raw_cash_balance")
        @Expose
        val rawCashBalance: Int = 0,
        @SerializedName("wallet_type")
        @Expose
        val walletType: String = "",
        @SerializedName("help_applink")
        @Expose
        val helpApplink: String = "",
        @SerializedName("tnc_applink")
        @Expose
        val tncApplink: String = "",
        @SerializedName("show_announcement")
        @Expose
        val isShowAnnouncement: Boolean = false
)

data class AbTag(
        @SerializedName("__typename")
        @Expose
        val typename: String = "",
        @SerializedName("tag")
        @Expose
        val tag: String = ""
)

data class WalletAction(
        @SerializedName("__typename")
        @Expose
        val typename: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("redirect_url")
        @Expose
        val redirectUrl: String = "",
        @SerializedName("applinks")
        @Expose
        val applinks: String = "",
        @SerializedName("visibility")
        @Expose
        val visibility: String = ""
)