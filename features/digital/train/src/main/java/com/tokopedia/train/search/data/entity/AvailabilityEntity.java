package com.tokopedia.train.search.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabilla on 3/9/18.
 */

public class AvailabilityEntity {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("available")
    @Expose
    private int available;

    public String getId() {
        return id;
    }

    public int getAvailable() {
        return available;
    }
}
