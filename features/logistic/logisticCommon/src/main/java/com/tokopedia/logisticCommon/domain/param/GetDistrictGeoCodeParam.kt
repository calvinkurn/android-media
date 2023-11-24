package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetDistrictGeoCodeParam(
    @SerializedName("latlng")
    val latLng: String = "",

    @SerializedName("is_manage_address_flow")
    val isManageAddressFlow: Boolean = false
) : GqlParam
