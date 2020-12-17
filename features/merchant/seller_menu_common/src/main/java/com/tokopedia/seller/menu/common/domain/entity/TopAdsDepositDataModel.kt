package com.tokopedia.seller.menu.common.domain.entity

import com.google.gson.annotations.SerializedName

data class TopAdsDepositDataModel(
        @SerializedName("topadsDashboardDeposits")
        val topAdsDashboardDeposits: TopAdsDashboardDeposits? = TopAdsDashboardDeposits()
)

data class TopAdsDashboardDeposits(
        @SerializedName("data")
        val depositData: DepositData? = DepositData(),
        @SerializedName("errors")
        val errors: List<TopAdsDepositError>? = listOf()
)

data class DepositData(
        @SerializedName("amount")
        val amount: Float? = 0f
)

data class TopAdsDepositError(
        @SerializedName("code")
        val code: String? = "",
        @SerializedName("title")
        val title: String? = "",
        @SerializedName("detail")
        val detail: String? = "",
        @SerializedName("object")
        val errorObject: TopAdsDashboardErrorObject? = TopAdsDashboardErrorObject()
)

data class TopAdsDashboardErrorObject(
        @SerializedName("type")
        val objectType: Int = 0,
        @SerializedName("text")
        val errorTextList: List<String> = listOf()
)