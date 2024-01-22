package com.tokopedia.targetedticker.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetTargetedTickerRequest(
    @SerializedName("input")
    val input: GetTargetedTickerParam = GetTargetedTickerParam()
) : GqlParam
data class GetTargetedTickerParam(
    @SerializedName("Page")
    val page: String = "",

    @SerializedName("Target")
    val target: List<GetTargetedTickerRequestTarget> = listOf()
) : GqlParam {

    data class GetTargetedTickerRequestTarget(
        @SerializedName("Type")
        val type: String = "",

        @SerializedName("Values")
        val value: List<String> = listOf()
    ) : GqlParam
}
