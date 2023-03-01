package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetTargetedTickerParam(
    @SerializedName("Page")
    val page: String = "",

    @SerializedName("Target")
    val target: List<GetTargetedTickerRequestTarget> = listOf()
) : GqlParam {

    companion object {
        val ADDRESS_LIST_OCC = "marketplace.address-list-occ"
        val ADDRESS_LIST_NON_OCC = "marketplace.address-list-non-occ"
    }
    data class GetTargetedTickerRequestTarget(
        @SerializedName("Type")
        val type: String = "",

        @SerializedName("Values")
        val value: List<String> = listOf()
    ) : GqlParam
}
