package com.tokopedia.shop.info.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticTypeFactory;

public class ShopInfoLogisticViewModel implements Visitable<ShopInfoLogisticTypeFactory> {

    private String shipmentImage;
    private String shipmentName;
    private String shipmentPackage;

    public String getShipmentImage() {
        return shipmentImage;
    }

    public void setShipmentImage(String shipmentImage) {
        this.shipmentImage = shipmentImage;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public String getShipmentPackage() {
        return shipmentPackage;
    }

    public void setShipmentPackage(String shipmentPackage) {
        this.shipmentPackage = shipmentPackage;
    }

    @Override
    public int type(ShopInfoLogisticTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
