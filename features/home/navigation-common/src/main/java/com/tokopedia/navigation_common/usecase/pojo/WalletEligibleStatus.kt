package com.tokopedia.navigation_common.usecase.pojo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletEligibleStatus(
    @Expose
    @SerializedName("is_eligible")
    val isEligible: Boolean = false,
    @Expose
    @SerializedName("wallet_code")
    val walletCode: String = ""
)