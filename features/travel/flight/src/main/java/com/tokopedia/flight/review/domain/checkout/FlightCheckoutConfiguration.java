package com.tokopedia.flight.review.domain.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 12/20/17.
 */

public class FlightCheckoutConfiguration {
    @SerializedName("price")
    @Expose
    private int price;

    public FlightCheckoutConfiguration() {
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
