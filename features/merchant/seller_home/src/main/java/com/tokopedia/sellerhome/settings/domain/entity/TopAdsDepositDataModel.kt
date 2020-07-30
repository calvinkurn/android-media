package com.tokopedia.sellerhome.settings.domain.entity

import com.google.gson.annotations.SerializedName

data class TopAdsDepositDataModel(
        @SerializedName("topadsDashboardDeposits")
        var topAdsDashboardDeposits: TopAdsDashboardDeposits? = TopAdsDashboardDeposits()
)

data class TopAdsDashboardDeposits(
        @SerializedName("data")
        var depositData: DepositData? = DepositData(),
        @SerializedName("errors")
        var errors: List<TopAdsDepositError>? = listOf()
)

data class DepositData(
        @SerializedName("amount")
        var amount: Float? = 0f
)

data class TopAdsDepositError(
        @SerializedName("code")
        var errorCode: String? = ""
)