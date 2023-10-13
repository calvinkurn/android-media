package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class PinpointValidationParam(
    @SerializedName("district_id")
    val districtId: Long = 0L,

    @SerializedName("latitude")
    val lat: String = "",

    @SerializedName("longitude")
    val long: String = "",

    @SerializedName("postal_code")
    val postalCode: String = ""
) : GqlParam
