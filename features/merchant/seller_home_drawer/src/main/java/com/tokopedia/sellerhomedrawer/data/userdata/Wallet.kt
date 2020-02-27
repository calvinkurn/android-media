package com.tokopedia.sellerhomedrawer.data.userdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerhomedrawer.data.userdata.wallet.AbTag
import com.tokopedia.sellerhomedrawer.data.userdata.wallet.Action

data class Wallet (

    @SerializedName("linked")
    @Expose
    var linked: Boolean? = false,
    @SerializedName("balance")
    @Expose
    var balance: String? = "",
    @SerializedName("rawBalance")
    @Expose
    var rawBalance: Int? = 0,
    @SerializedName("text")
    @Expose
    var text: String? = "",
    @SerializedName("total_balance")
    @Expose
    val totalBalance: String? = "",
    @SerializedName("raw_total_balance")
    @Expose
    val rawTotalBalance: Int? = 0,
    @SerializedName("hold_balance")
    @Expose
    val holdBalance: String? = "",
    @SerializedName("raw_hold_balance")
    @Expose
    val rawHoldBalance: Int? = 0,
    @SerializedName("redirect_url")
    @Expose
    var redirectUrl: String? = "",
    @SerializedName("applinks")
    @Expose
    var applinks: String? = "",
    @SerializedName("ab_tags")
    @Expose
    val abTags: List<AbTag>? = listOf(),
    @SerializedName("action")
    @Expose
    var action: Action? = Action()
)
