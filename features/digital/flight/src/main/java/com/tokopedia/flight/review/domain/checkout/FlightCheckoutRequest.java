package com.tokopedia.flight.review.domain.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 12/20/17.
 */

public class FlightCheckoutRequest {
    @SerializedName("data")
    @Expose
    private FlightCheckoutData data;

    public FlightCheckoutData getData() {
        return data;
    }

    public void setData(FlightCheckoutData data) {
        this.data = data;
    }
}
