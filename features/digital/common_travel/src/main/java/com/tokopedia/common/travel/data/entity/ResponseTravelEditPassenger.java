package com.tokopedia.common.travel.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 23/11/18.
 */
public class ResponseTravelEditPassenger {

    @SerializedName("travelUpdatePassenger")
    @Expose
    private TravelPassengerEntity travelPassengerEntity;

    public TravelPassengerEntity getTravelPassengerEntity() {
        return travelPassengerEntity;
    }
}
