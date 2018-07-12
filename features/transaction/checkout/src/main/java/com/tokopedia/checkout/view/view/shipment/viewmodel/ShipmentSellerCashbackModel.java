package com.tokopedia.checkout.view.view.shipment.viewmodel;

import com.tokopedia.checkout.view.view.shipment.ShipmentData;

public class ShipmentSellerCashbackModel implements ShipmentData {

    private String sellerCashback;
    private boolean visible;

    public ShipmentSellerCashbackModel() {
    }

    public String getSellerCashback() {
        return sellerCashback;
    }

    public void setSellerCashback(String sellerCashback) {
        this.sellerCashback = sellerCashback;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
