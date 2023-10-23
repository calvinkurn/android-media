package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetDistrictCenterParam(
    @SerializedName("districtId")
    val keyword: Long = 0L
) : GqlParam
