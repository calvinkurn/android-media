package com.tokopedia.flight.booking.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by alvarisi on 11/14/17.
 */

public class FlightRequest {
    @SerializedName("adult")
    @Expose
    private int adult;
    @SerializedName("child")
    @Expose
    private int child;
    @SerializedName("infant")
    @Expose
    private int infant;
    @SerializedName("class")
    @Expose
    private int classFlight;
    @SerializedName("combo_key")
    @Expose
    private String comboKey;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("price_currency")
    @Expose
    private int priceCurrency;
    @SerializedName("destination")
    @Expose
    private List<CartAirportRequest> destinations;

    public FlightRequest() {
    }

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    public int getInfant() {
        return infant;
    }

    public void setInfant(int infant) {
        this.infant = infant;
    }

    public int getClassFlight() {
        return classFlight;
    }

    public void setClassFlight(int classFlight) {
        this.classFlight = classFlight;
    }

    public String getComboKey() {
        return comboKey;
    }

    public void setComboKey(String comboKey) {
        this.comboKey = comboKey;
    }

    public List<CartAirportRequest> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<CartAirportRequest> destinations) {
        this.destinations = destinations;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(int priceCurrency) {
        this.priceCurrency = priceCurrency;
    }
}
