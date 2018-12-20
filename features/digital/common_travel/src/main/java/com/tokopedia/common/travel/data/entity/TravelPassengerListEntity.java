package com.tokopedia.common.travel.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 14/08/18.
 */
public class TravelPassengerListEntity {

    @SerializedName("passengerList")
    @Expose
    private List<TravelPassengerEntity> travelPassengerEntityList;

    public List<TravelPassengerEntity> getTravelPassengerEntityList() {
        return travelPassengerEntityList;
    }
}
