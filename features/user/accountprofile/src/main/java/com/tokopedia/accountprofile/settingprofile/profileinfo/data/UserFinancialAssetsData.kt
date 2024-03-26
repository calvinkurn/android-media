package com.tokopedia.accountprofile.settingprofile.profileinfo.data

import com.google.gson.annotations.SerializedName

data class UserFinancialAssetsData(
    @SerializedName("checkUserFinancialAssets")
    val checkUserFinancialAssets: CheckUserFinancialAssets = CheckUserFinancialAssets()
)

data class CheckUserFinancialAssets(
    @SerializedName("detail")
    val detail: Detail = Detail(),

    @SerializedName("has_financial_assets")
    val hasFinancialAssets: Boolean = false
)

data class Detail(
    @SerializedName("has_deposit_balance")
    val hasDepositBalance: Boolean = false,

    @SerializedName("has_ongoing_trx")
    val hasOngoingTrx: Boolean = false,

    @SerializedName("has_loan")
    val hasLoan: Boolean = false,

    @SerializedName("has_egold")
    val hasEgold: Boolean = false,

    @SerializedName("has_mutual_fund")
    val hasMutualFund: Boolean = false
)
