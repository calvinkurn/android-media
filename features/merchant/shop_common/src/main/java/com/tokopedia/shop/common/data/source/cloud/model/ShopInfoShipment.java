
package com.tokopedia.shop.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopInfoShipment {

    @SerializedName("shipment_available")
    @Expose
    private long shipmentAvailable;
    @SerializedName("shipment_code")
    @Expose
    private String shipmentCode;
    @SerializedName("shipment_id")
    @Expose
    private String shipmentId;
    @SerializedName("shipment_image")
    @Expose
    private String shipmentImage;
    @SerializedName("shipment_is_pickup")
    @Expose
    private long shipmentIsPickup;
    @SerializedName("shipment_name")
    @Expose
    private String shipmentName;
    @SerializedName("shipment_package")
    @Expose
    private List<ShopInfoShipmentPackage> shipmentPackage = null;
    @SerializedName("shipping_max_add_fee")
    @Expose
    private long shippingMaxAddFee;

    public long getShipmentAvailable() {
        return shipmentAvailable;
    }

    public void setShipmentAvailable(long shipmentAvailable) {
        this.shipmentAvailable = shipmentAvailable;
    }

    public String getShipmentCode() {
        return shipmentCode;
    }

    public void setShipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentImage() {
        return shipmentImage;
    }

    public void setShipmentImage(String shipmentImage) {
        this.shipmentImage = shipmentImage;
    }

    public long getShipmentIsPickup() {
        return shipmentIsPickup;
    }

    public void setShipmentIsPickup(long shipmentIsPickup) {
        this.shipmentIsPickup = shipmentIsPickup;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public List<ShopInfoShipmentPackage> getShipmentPackage() {
        return shipmentPackage;
    }

    public void setShipmentPackage(List<ShopInfoShipmentPackage> shipmentPackage) {
        this.shipmentPackage = shipmentPackage;
    }

    public long getShippingMaxAddFee() {
        return shippingMaxAddFee;
    }

    public void setShippingMaxAddFee(long shippingMaxAddFee) {
        this.shippingMaxAddFee = shippingMaxAddFee;
    }

}
