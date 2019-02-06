package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 23/11/18.
 */
class ResponseTravelEditPassenger(
    @SerializedName("travelUpdatePassenger")
    @Expose
    val travelPassengerEntity: TravelPassengerEntity)
