package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetAutoCompleteParam(
    @SerializedName("param")
    val keyword: String = "",

    @SerializedName("latlng")
    val latLng: String = "",

    @SerializedName("is_manage_address_flow")
    val isManageAddressFlow: Boolean = false,

    @SerializedName("with_administrative")
    val withAdministrative: Boolean = true
) : GqlParam
