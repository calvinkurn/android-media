package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetZipCodeParam(
    @SerializedName("query")
    val districtId: String = "",

    @SerializedName("page")
    val page: String = "1"
) : GqlParam
