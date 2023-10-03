package com.tokopedia.logisticaddaddress.domain.model.district_recommendation

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetDistrictRecomParam(
    @SerializedName("query")
    val query: String = "",
    @SerializedName("page")
    val page: String = "1"
) : GqlParam
