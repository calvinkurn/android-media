package com.tokopedia.sellerhomedrawer.data.userdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.core.drawer2.data.pojo.Action
import com.tokopedia.sellerhomedrawer.data.userdata.wallet.AbTag

class Wallet {

    @SerializedName("linked")
    @Expose
    var linked: Boolean? = null
    @SerializedName("balance")
    @Expose
    var balance: String? = null
    @SerializedName("rawBalance")
    @Expose
    var rawBalance: Int? = null
    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("total_balance")
    @Expose
    val totalBalance: String? = null
    @SerializedName("raw_total_balance")
    @Expose
    val rawTotalBalance: Int? = null
    @SerializedName("hold_balance")
    @Expose
    val holdBalance: String? = null
    @SerializedName("raw_hold_balance")
    @Expose
    val rawHoldBalance: Int? = null
    @SerializedName("redirect_url")
    @Expose
    var redirectUrl: String? = null
    @SerializedName("applinks")
    @Expose
    var applinks: String? = null
    @SerializedName("ab_tags")
    @Expose
    val abTags: List<AbTag>? = null
    @SerializedName("action")
    @Expose
    var action: Action? = null
}
