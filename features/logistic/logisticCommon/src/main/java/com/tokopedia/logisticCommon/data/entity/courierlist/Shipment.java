package com.tokopedia.logisticCommon.data.entity.courierlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class Shipment {

    @SerializedName("shipment_available")
    @Expose
    private Integer shipmentAvailable;
    @SerializedName("shipment_image")
    @Expose
    private String shipmentImage;
    @SerializedName("shipping_max_add_fee")
    @Expose
    private Integer shippingMaxAddFee;
    @SerializedName("shipment_package")
    @Expose
    private List<ShipmentPackage> shipmentPackage = new ArrayList<ShipmentPackage>();
    @SerializedName("shipment_name")
    @Expose
    private String shipmentName;
    @SerializedName("shipment_id")
    @Expose
    private String shipmentId;

    /**
     *
     * @return
     *     The shipmentAvailable
     */
    public Integer getShipmentAvailable() {
        return shipmentAvailable;
    }

    /**
     *
     * @param shipmentAvailable
     *     The shipment_available
     */
    public void setShipmentAvailable(Integer shipmentAvailable) {
        this.shipmentAvailable = shipmentAvailable;
    }

    /**
     *
     * @return
     *     The shipmentImage
     */
    public String getShipmentImage() {
        return shipmentImage;
    }

    /**
     *
     * @param shipmentImage
     *     The shipment_image
     */
    public void setShipmentImage(String shipmentImage) {
        this.shipmentImage = shipmentImage;
    }

    /**
     *
     * @return
     *     The shippingMaxAddFee
     */
    public Integer getShippingMaxAddFee() {
        return shippingMaxAddFee;
    }

    /**
     *
     * @param shippingMaxAddFee
     *     The shipping_max_add_fee
     */
    public void setShippingMaxAddFee(Integer shippingMaxAddFee) {
        this.shippingMaxAddFee = shippingMaxAddFee;
    }

    /**
     *
     * @return
     *     The shipmentPackage
     */
    public List<ShipmentPackage> getShipmentPackage() {
        return shipmentPackage;
    }

    /**
     *
     * @param shipmentPackage
     *     The shipment_package
     */
    public void setShipmentPackage(List<ShipmentPackage> shipmentPackage) {
        this.shipmentPackage = shipmentPackage;
    }

    /**
     *
     * @return
     *     The shipmentName
     */
    public String getShipmentName() {
        return shipmentName;
    }

    /**
     *
     * @param shipmentName
     *     The shipment_name
     */
    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    /**
     *
     * @return
     *     The shipmentId
     */
    public String getShipmentId() {
        return shipmentId;
    }

    /**
     *
     * @param shipmentId
     *     The shipment_id
     */
    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

}
