package com.tokopedia.common.travel.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 14/08/18.
 */
public class ResponseTravelPassengerList {

    @SerializedName("travelPassengers")
    @Expose
    private TravelPassengerListEntity travelPassengerListEntity;

    public TravelPassengerListEntity getTravelPassengerListEntity() {
        return travelPassengerListEntity;
    }
}
