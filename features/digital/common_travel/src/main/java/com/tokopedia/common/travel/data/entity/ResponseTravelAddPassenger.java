package com.tokopedia.common.travel.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 21/11/18.
 */
public class ResponseTravelAddPassenger {

    @SerializedName("travelAddPassenger")
    @Expose
    private TravelPassengerEntity travelPassengerEntity;

    public TravelPassengerEntity getTravelPassengerEntity() {
        return travelPassengerEntity;
    }
}
