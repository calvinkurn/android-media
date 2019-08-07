package com.tokopedia.checkout.view.feature.shipment.viewmodel;

import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentData;

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
