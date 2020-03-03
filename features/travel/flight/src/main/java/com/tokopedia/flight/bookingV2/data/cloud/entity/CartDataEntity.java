package com.tokopedia.flight.bookingV2.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 11/13/17.
 */

public class CartDataEntity {
    @SerializedName("data")
    @Expose
    private CartEntity data;
    @SerializedName("included")
    @Expose
    private String id;
}

