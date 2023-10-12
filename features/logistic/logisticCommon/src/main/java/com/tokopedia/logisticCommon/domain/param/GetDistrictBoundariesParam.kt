package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetDistrictBoundariesParam(
    @SerializedName("districtId")
    val districtId: Long = 0L
) : GqlParam
