package com.tokopedia.flight.orderlist.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 11/28/2017.
 */

public class FlightOrderError {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("title")
    @Expose
    private String title;

    public FlightOrderError(String id) {
        this.id = id;
    }

    public FlightOrderError() {
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightOrderError && ((FlightOrderError) obj).getId().equalsIgnoreCase(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return title;
    }
}
