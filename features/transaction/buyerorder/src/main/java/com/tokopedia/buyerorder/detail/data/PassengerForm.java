package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PassengerForm {
    @SerializedName("passenger_informations")
    @Expose
    private List<PassengerInformation> passengerInformations;

    public List<PassengerInformation> getPassengerInformations() {
        return passengerInformations;
    }
}
