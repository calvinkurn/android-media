package com.tokopedia.purchase_platform.common.feature.sellercashback;

/**
 * @author Irfan Khoirul on 12/07/18.
 */

public class ShipmentSellerCashbackModel {

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
