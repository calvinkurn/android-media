package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FlightCountryEntity(
    @SerializedName("id")
    @Expose
    var id: String,
    @SerializedName("attributes")
    @Expose
    var attributesEntity: FlightCountryAttributesEntity)
