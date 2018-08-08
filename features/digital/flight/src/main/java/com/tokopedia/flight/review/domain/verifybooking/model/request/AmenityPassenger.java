
package com.tokopedia.flight.review.domain.verifybooking.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AmenityPassenger {

    @SerializedName("arrival_id")
    @Expose
    private String arrivalId;
    @SerializedName("departure_id")
    @Expose
    private String departureId;
    @SerializedName("journey_id")
    @Expose
    private String journeyId;
    @SerializedName("amenity_type")
    @Expose
    private int amenityType;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("item_id")
    @Expose
    private String itemId;

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public int getAmenityType() {
        return amenityType;
    }

    public void setAmenityType(int amenityType) {
        this.amenityType = amenityType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(String arrivalId) {
        this.arrivalId = arrivalId;
    }

    public String getDepartureId() {
        return departureId;
    }

    public void setDepartureId(String departureId) {
        this.departureId = departureId;
    }
}
