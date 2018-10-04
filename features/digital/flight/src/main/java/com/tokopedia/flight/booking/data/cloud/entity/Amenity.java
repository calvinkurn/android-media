package com.tokopedia.flight.booking.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 11/13/17.
 */

public class Amenity {
    public static final int LUGGAGE = 1;
    public static final int MEAL = 2;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("amenity_type")
    @Expose
    private int type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("journey_id")
    @Expose
    private String journeyId;
    @SerializedName("arrival_id")
    @Expose
    private String arrivalId;
    @SerializedName("departure_id")
    @Expose
    private String departureId;
    @SerializedName("items")
    @Expose
    private List<AmenityItem> items;

    public Amenity() {
    }

    public String getKey() {
        return key;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public List<AmenityItem> getItems() {
        return items;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public String getArrivalId() {
        return arrivalId;
    }

    public String getDepartureId() {
        return departureId;
    }
}
