package com.tokopedia.core.shipping.model.kero;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Herdi_WORK on 22.09.16.
 */

public class Product {
    @SerializedName("shipper_product_id")
    @Expose
    private String shipperProductId;
    @SerializedName("shipper_product_name")
    @Expose
    private String shipperProductName;
    @SerializedName("shipper_product_desc")
    @Expose
    private String shipperProductDesc;
    @SerializedName("is_show_map")
    @Expose
    private Integer isShowMap;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("formatted_price")
    @Expose
    private String formattedPrice;
    @SerializedName("check_sum")
    @Expose
    private String checkSum;
    @SerializedName("ut")
    @Expose
    private String ut;

    /**
     * @return The shipperProductId
     */
    public String getShipperProductId() {
        return shipperProductId;
    }

    /**
     * @param shipperProductId The shipper_product_id
     */
    public void setShipperProductId(String shipperProductId) {
        this.shipperProductId = shipperProductId;
    }

    /**
     * @return The shipperProductName
     */
    public String getShipperProductName() {
        return shipperProductName;
    }

    /**
     * @param shipperProductName The shipper_product_name
     */
    public void setShipperProductName(String shipperProductName) {
        this.shipperProductName = shipperProductName;
    }

    /**
     * @return The shipperProductDesc
     */
    public String getShipperProductDesc() {
        return shipperProductDesc;
    }

    /**
     * @param shipperProductDesc The shipper_product_desc
     */
    public void setShipperProductDesc(String shipperProductDesc) {
        this.shipperProductDesc = shipperProductDesc;
    }

    /**
     * @return The isShowMap
     */
    public Integer getIsShowMap() {
        return isShowMap;
    }

    /**
     * @param isShowMap The is_show_map
     */
    public void setIsShowMap(Integer isShowMap) {
        this.isShowMap = isShowMap;
    }

    /**
     * @return The price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * @return The formattedPrice
     */
    public String getFormattedPrice() {
        return formattedPrice;
    }

    /**
     * @param formattedPrice The formatted_price
     */
    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    /**
     * @return The checkSum
     */
    public String getCheckSum() {
        return checkSum;
    }

    /**
     * @param checkSum The check_sum
     */
    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    /**
     * @return The ut
     */
    public String getUt() {
        return ut;
    }

    /**
     * @param ut The ut
     */
    public void setUt(String ut) {
        this.ut = ut;
    }

    @Override
    public String toString() {
        return getShipperProductName();
    }

    public static Product createSelectionInfo(String info) {
        Product shipment = new Product();
        shipment.setShipperProductName(info);
        shipment.setShipperProductId("0");
        shipment.setIsShowMap(0);
        shipment.setPrice(0);
        shipment.setFormattedPrice("0");
        return shipment;
    }

}
