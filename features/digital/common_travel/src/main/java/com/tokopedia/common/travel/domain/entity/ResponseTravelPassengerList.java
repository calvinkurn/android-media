package com.tokopedia.common.travel.domain.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 14/08/18.
 */
public class ResponseTravelPassengerList {

    @SerializedName("kaiPassengers")
    @Expose
    private TravelPassengerListEntity travelPassengerListEntity;

    public TravelPassengerListEntity getTravelPassengerListEntity() {
        return travelPassengerListEntity;
    }
}
