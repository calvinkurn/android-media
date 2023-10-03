package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetAddressParam(
    @SerializedName("limit")
    val limit: Int = 5,

    @SerializedName("page")
    val page: Int = 1,

    @SerializedName("show_address")
    val showAddress: Boolean = false
) : GqlParam

data class KeroGetAddressCornerInput(
    @SerializedName("input")
    val input: GetAddressParam = GetAddressParam()
) : GqlParam
