package com.tokopedia.logisticCommon.data.entity.trackingshipment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alifa on 10/12/2016.
 */

public class TrackingResponse {

    private static final String TAG = TrackingResponse.class.getSimpleName();
    @SerializedName("track_order")
    @Expose
    private TrackOrder trackOrder;
    @SerializedName("page")
    @Expose
    private Page page;

    public TrackOrder getTrackOrder() {
        return trackOrder;
    }

    public void setTrackOrder(TrackOrder trackOrder) {
        this.trackOrder = trackOrder;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
