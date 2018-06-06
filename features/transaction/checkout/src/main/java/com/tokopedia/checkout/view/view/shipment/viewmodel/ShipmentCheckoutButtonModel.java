package com.tokopedia.checkout.view.view.shipment.viewmodel;

import com.tokopedia.checkout.view.view.shipment.ShipmentData;

public class ShipmentCheckoutButtonModel implements ShipmentData {
    private String totalPayment;
    private boolean ableToCheckout;

    public boolean isAbleToCheckout() {
        return ableToCheckout;
    }

    public void setAbleToCheckout(boolean ableToCheckout) {
        this.ableToCheckout = ableToCheckout;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

}
