package com.tokopedia.checkout.view.feature.shippingoptions.viewmodel;

import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentOptionData;

/**
 * @author Irfan Khoirul on 04/05/18.
 */

public class ShipmentTypeData implements ShipmentOptionData {

    private String shipmentType;
    private String etd;

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }
}
