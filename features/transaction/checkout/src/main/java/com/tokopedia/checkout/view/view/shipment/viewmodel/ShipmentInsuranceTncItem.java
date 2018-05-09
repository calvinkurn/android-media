package com.tokopedia.checkout.view.view.shipment.viewmodel;

import com.tokopedia.checkout.view.view.shipment.ShipmentData;

public class ShipmentInsuranceTncItem implements ShipmentData {

    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
