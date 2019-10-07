package com.tokopedia.shop.common.graphql.data.membershipclaimbenefit


import com.google.gson.annotations.SerializedName

data class MembershipClaimData(
    @SerializedName("resultStatus")
    val resultStatus: ResultStatus = ResultStatus(),
    @SerializedName("subTitle")
    val subTitle: String = "",
    @SerializedName("title")
    val title: String = ""
)