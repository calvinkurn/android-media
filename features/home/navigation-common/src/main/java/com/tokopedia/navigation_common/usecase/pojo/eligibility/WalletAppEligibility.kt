package com.tokopedia.navigation_common.usecase.pojo.eligibility


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletAppEligibility(
    @Expose
    @SerializedName("walletappGetWalletEligible")
    val walletappGetWalletEligible: WalletappGetWalletEligible = WalletappGetWalletEligible()
)