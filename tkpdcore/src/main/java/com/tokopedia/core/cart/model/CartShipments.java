
package com.tokopedia.core.cart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartShipments {

    @SerializedName("shipment_image")
    @Expose
    private String shipmentImage;
    @SerializedName("shipment_notes")
    @Expose
    private String shipmentNotes;
    @SerializedName("shipment_id")
    @Expose
    private String shipmentId;
    @SerializedName("shipment_package_name")
    @Expose
    private String shipmentPackageName;
    @SerializedName("shipment_name")
    @Expose
    private String shipmentName;
    @SerializedName("shipment_package_id")
    @Expose
    private String shipmentPackageId;
    @SerializedName("is_pick_up")
    @Expose
    private int isPickUp;

    public String getShipmentImage() {
        return shipmentImage;
    }

    public void setShipmentImage(String shipmentImage) {
        this.shipmentImage = shipmentImage;
    }

    public String getShipmentNotes() {
        return shipmentNotes;
    }

    public void setShipmentNotes(String shipmentNotes) {
        this.shipmentNotes = shipmentNotes;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentPackageName() {
        return shipmentPackageName;
    }

    public void setShipmentPackageName(String shipmentPackageName) {
        this.shipmentPackageName = shipmentPackageName;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public String getShipmentPackageId() {
        return shipmentPackageId;
    }

    public int getPickUp() {
        return isPickUp;
    }

    public void setShipmentPackageId(String shipmentPackageId) {
        this.shipmentPackageId = shipmentPackageId;
    }
}
