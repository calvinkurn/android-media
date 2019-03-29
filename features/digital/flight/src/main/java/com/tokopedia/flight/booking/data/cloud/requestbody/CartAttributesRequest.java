package com.tokopedia.flight.booking.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 11/14/17.
 */

public class CartAttributesRequest {
    @SerializedName("flight")
    @Expose
    private FlightRequest flight;
    @SerializedName("ip_address")
    @Expose
    private String ipAddress;
    @SerializedName("user_agent")
    @Expose
    private String userAgent;
    @SerializedName("did")
    @Expose
    private int did;

    public CartAttributesRequest() {
    }

    public FlightRequest getFlight() {
        return flight;
    }

    public void setFlight(FlightRequest flight) {
        this.flight = flight;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }
}
