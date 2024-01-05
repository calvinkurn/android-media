package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ReverseGeocodeParam(
    @SerializedName("latlng")
    val latlng: String = "",
    @SerializedName("err")
    val error: Boolean = true,
    @SerializedName("is_manage_address_flow")
    val isManageAddressFlow: Boolean = false
) : GqlParam
