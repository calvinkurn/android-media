package com.tokopedia.navigation_common.usecase.pojo.eligibility


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
    @Expose
    @SerializedName("is_eligible")
    val isEligible: Boolean = false,
    @Expose
    @SerializedName("wallet_code")
    val walletCode: String = ""
)