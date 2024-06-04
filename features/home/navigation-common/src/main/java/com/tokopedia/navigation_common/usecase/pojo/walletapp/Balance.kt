package com.tokopedia.navigation_common.usecase.pojo.walletapp


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Balance(
    @Expose
    @SerializedName("active")
    val active: Boolean = false,
    @Expose
    @SerializedName("amount")
    val amount: Int = 0,
    @Expose
    @SerializedName("amount_fmt")
    val amountFmt: String = "",
    @Expose
    @SerializedName("message")
    val message: String = "",
    @Expose
    @SerializedName("wallet_code")
    val walletCode: String = "",
)
