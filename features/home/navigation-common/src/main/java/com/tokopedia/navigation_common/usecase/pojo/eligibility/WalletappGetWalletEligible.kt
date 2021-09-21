package com.tokopedia.navigation_common.usecase.pojo.eligibility


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletappGetWalletEligible(
    @Expose
    @SerializedName("code")
    val code: String = "",
    @Expose
    @SerializedName("data")
    val `data`: List<Data> = listOf(),
    @Expose
    @SerializedName("message")
    val message: String = ""
)