package com.tokopedia.checkout.view.feature.shipment.viewmodel;

import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentData;

/**
 * @author Irfan Khoirul on 12/07/18.
 */

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
