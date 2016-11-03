
package com.tokopedia.tkpd.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class OrderShipment {

    @SerializedName("shipment_logo")
    @Expose
    String shipmentLogo;
    @SerializedName("shipment_package_id")
    @Expose
    String shipmentPackageId;
    @SerializedName("shipment_id")
    @Expose
    String shipmentId;
    @SerializedName("shipment_product")
    @Expose
    String shipmentProduct;
    @SerializedName("shipment_name")
    @Expose
    String shipmentName;

    /**
     * 
     * @return
     *     The shipmentLogo
     */
    public String getShipmentLogo() {
        return shipmentLogo;
    }

    /**
     * 
     * @param shipmentLogo
     *     The shipment_logo
     */
    public void setShipmentLogo(String shipmentLogo) {
        this.shipmentLogo = shipmentLogo;
    }

    /**
     * 
     * @return
     *     The shipmentPackageId
     */
    public String getShipmentPackageId() {
        return shipmentPackageId;
    }

    /**
     * 
     * @param shipmentPackageId
     *     The shipment_package_id
     */
    public void setShipmentPackageId(String shipmentPackageId) {
        this.shipmentPackageId = shipmentPackageId;
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

    /**
     * 
     * @return
     *     The shipmentProduct
     */
    public String getShipmentProduct() {
        return shipmentProduct;
    }

    /**
     * 
     * @param shipmentProduct
     *     The shipment_product
     */
    public void setShipmentProduct(String shipmentProduct) {
        this.shipmentProduct = shipmentProduct;
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

}
