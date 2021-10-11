package com.tokopedia.logisticCommon.data.response

import com.google.gson.annotations.SerializedName

data class GetDistrictBoundaryResponse(
        @SerializedName("keroGetDistrictBoundaryArray")
        val keroGetDistrictBoundaryArray: KeroGetDistrictBoundaryArray = KeroGetDistrictBoundaryArray()
)

data class KeroGetDistrictBoundaryArray(

        @SerializedName("geometry")
        val geometry: Geometry = Geometry()
)

data class Geometry(
        @SerializedName("coordinates")
        val coordinates: List<List<List<List<Double>>>> = emptyList()
)