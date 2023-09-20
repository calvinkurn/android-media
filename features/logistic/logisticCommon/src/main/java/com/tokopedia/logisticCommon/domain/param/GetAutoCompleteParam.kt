package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName

data class GetAutoCompleteParam(
    @SerializedName("param")
    val keyword: String = "",

    @SerializedName("latlng")
    val latLng: String = "",

    @SerializedName("is_manage_address_flow")
    val isManageAddressFlow: Boolean = true
)
