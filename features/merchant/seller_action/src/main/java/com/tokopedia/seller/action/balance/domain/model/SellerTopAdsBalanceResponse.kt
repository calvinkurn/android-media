package com.tokopedia.seller.action.balance.domain.model

import com.google.gson.annotations.SerializedName

data class SellerTopAdsBalanceResponse(
        @SerializedName("topadsDashboardDeposits")
        val sellerActionTopadsDashboardDeposits: SellerActionTopadsDashboardDeposits? = SellerActionTopadsDashboardDeposits()
)

data class SellerActionTopadsDashboardDeposits(
        @SerializedName("data")
        val data: SellerActionTopAdsDashboardData? = SellerActionTopAdsDashboardData(),
        @SerializedName("errors")
        val errors: List<SellerActionTopAdsDashboardError>? = listOf()
)

data class SellerActionTopAdsDashboardData(
        @SerializedName("amount_fmt")
        val amountFmt: String? = ""
)

data class SellerActionTopAdsDashboardError(
        @SerializedName("detail")
        val errorDetail: String? = "",
        @SerializedName("object")
        val errorObject: SellerActionTopAdsErrorObject? = SellerActionTopAdsErrorObject()
)

data class SellerActionTopAdsErrorObject(
        @SerializedName("text")
        val errorText: String? = ""
)