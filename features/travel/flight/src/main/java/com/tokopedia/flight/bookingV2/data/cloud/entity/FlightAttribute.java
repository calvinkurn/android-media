package com.tokopedia.flight.bookingV2.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 11/13/17.
 */

public class FlightAttribute {
    @SerializedName("new_price")
    @Expose
    private List<NewFarePrice> newPrices;
    @SerializedName("is_domestic")
    @Expose
    private boolean isDomestic;
    @SerializedName("refresh_time")
    @Expose
    private int refreshTime;
    @SerializedName("amenities")
    @Expose
    private List<Amenity> amenities;
    @SerializedName("voucher")
    @Expose
    private Voucher voucher;
    @SerializedName("mandatory_dob")
    @Expose
    private boolean mandatoryDob;

    public FlightAttribute() {
    }

    public List<NewFarePrice> getNewPrices() {
        return newPrices;
    }

    public boolean isDomestic() {
        return isDomestic;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public boolean isMandatoryDob() {
        return mandatoryDob;
    }
}
