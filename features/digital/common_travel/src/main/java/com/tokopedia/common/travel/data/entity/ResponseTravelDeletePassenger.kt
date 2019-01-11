package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 22/11/18.
 */
class ResponseTravelDeletePassenger(
    @SerializedName("travelDeletePassenger")
    @Expose
    val travelPassengerEntity: TravelPassengerEntity)
