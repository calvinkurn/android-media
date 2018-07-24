package com.tokopedia.flight.review.domain.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvarisi on 12/20/17.
 */

public class FlightCheckoutItem {
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("meta_data")
    @Expose
    private FlightCheckoutMetaData metaData;
    @SerializedName("configuration")
    @Expose
    private FlightCheckoutConfiguration configuration;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public FlightCheckoutMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(FlightCheckoutMetaData metaData) {
        this.metaData = metaData;
    }

    public FlightCheckoutConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(FlightCheckoutConfiguration configuration) {
        this.configuration = configuration;
    }
}
