package com.tokopedia.logisticaddaddress.domain.model.district_boundary

import com.google.gson.annotations.SerializedName

data class KeroGetDistrictBoundaryArray(

    @SerializedName("geometry")
    val geometry: Geometry = Geometry()
)
