package com.tokopedia.checkout.view.feature.shipment.viewmodel;

import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentData;

/**
 * Created by fajarnuha on 06/12/18.
 */
public class ShipmentNotifierModel implements ShipmentData {

    private String message;
    private String url;

    public ShipmentNotifierModel() {
    }

    public ShipmentNotifierModel(String message, String url) {
        this.message = message;
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
