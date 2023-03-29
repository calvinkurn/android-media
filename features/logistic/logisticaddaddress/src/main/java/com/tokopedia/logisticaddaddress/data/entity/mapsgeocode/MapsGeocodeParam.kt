package com.tokopedia.logisticaddaddress.data.entity.mapsgeocode

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class MapsGeocodeParam(
    @SerializedName("input")
    val input: MapsGeocodeDataParam = MapsGeocodeDataParam()
) : GqlParam {
    data class MapsGeocodeDataParam(
        @SerializedName("latlng")
        val latlng: String = "",
        @SerializedName("address")
        val address: String = ""
    ) : GqlParam
}

