package com.tokopedia.logisticaddaddress.domain.model.district_boundary

import com.google.gson.annotations.SerializedName

data class Geometry(

    @SerializedName("coordinates")
    val coordinates: List<List<List<List<Double>>>> = emptyList()
)
