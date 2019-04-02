package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 14/08/18.
 */
class ResponseTravelPassengerList(
    @SerializedName("travelPassengers")
    @Expose
    val travelPassengerListEntity: TravelPassengerListEntity)
