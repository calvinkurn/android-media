package com.tokopedia.navigation_common.usecase.pojo.walletapp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReserveBalance(
    @Expose
    @SerializedName("wallet_code")
    val walletCode: String = "",
    @Expose
    @SerializedName("wallet_code_fmt")
    val walletCodeFmt: String = "",
    @Expose
    @SerializedName("amount")
    val amount: Int = 0,
    @Expose
    @SerializedName("amount_fmt")
    val amountFmt: String = ""
)