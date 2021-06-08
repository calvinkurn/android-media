package com.tokopedia.flight.common.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 11/28/2017.
 */

public class FlightError {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("head")
    @Expose
    private String head;
    @SerializedName("message")
    @Expose
    private String message;

    public FlightError(String id) {
        this.id = id;
    }

    public FlightError() {
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightError && ((FlightError) obj).getId().equalsIgnoreCase(id);
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
