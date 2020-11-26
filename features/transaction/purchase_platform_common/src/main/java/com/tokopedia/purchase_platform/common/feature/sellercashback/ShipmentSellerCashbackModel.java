package com.tokopedia.purchase_platform.common.feature.sellercashback;

/**
 * @author Irfan Khoirul on 12/07/18.
 */

public class ShipmentSellerCashbackModel {

    private int sellerCashback;
    private String sellerCashbackFmt;
    private boolean visible;

    public ShipmentSellerCashbackModel() {
    }

    public String getSellerCashbackFmt() {
        return sellerCashbackFmt;
    }

    public void setSellerCashbackFmt(String sellerCashbackFmt) {
        this.sellerCashbackFmt = sellerCashbackFmt;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getSellerCashback() {
        return sellerCashback;
    }

    public void setSellerCashback(int sellerCashback) {
        this.sellerCashback = sellerCashback;
    }
}
