package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

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
