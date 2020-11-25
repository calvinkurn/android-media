package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PassengerInformation {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("title")
    @Expose
    private String title;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }
}
