package com.tokopedia.ovop2p.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Wallet (
    @SerializedName("balance")
    @Expose
    val balance: String = "",
    @SerializedName("rawBalance")
    @Expose
    val rawBalance: Int = 0,
    @SerializedName("cash_balance")
    @Expose
    val cashBalance: String = "",
    @SerializedName("raw_cash_balance")
    @Expose
    val rawCashBalance: Int = 0,
    @SerializedName("point_balance")
    @Expose
    val pointBalance: String = "",
    @SerializedName("raw_point_balance")
    @Expose
    val rawPointBalance: Int = 0,
    @SerializedName("errors")
    @Expose
    val errors: List<WalletErrors>? = null
)
