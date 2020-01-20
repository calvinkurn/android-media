package com.tokopedia.purchase_platform.features.checkout.view.viewmodel;

import com.tokopedia.logisticcart.shipping.model.ShipmentData;

/**
 * @author Irfan Khoirul on 09/05/18.
 */

public class ShipmentInsuranceTncModel implements ShipmentData {

    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
