package com.tokopedia.tkpd.shipping.model.kero;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Herdi_WORK on 22.09.16.
 */

public class Attribute {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("shipper_id")
    @Expose
    private String shipperId;
    @SerializedName("shipper_name")
    @Expose
    private String shipperName;
    @SerializedName("origin_id")
    @Expose
    private Integer originId;
    @SerializedName("destination_id")
    @Expose
    private Integer destinationId;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("products")
    @Expose
    private List<Product> products = new ArrayList<Product>();

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The shipperId
     */
    public String getShipperId() {
        return shipperId;
    }

    /**
     *
     * @param shipperId
     * The shipper_id
     */
    public void setShipperId(String shipperId) {
        this.shipperId = shipperId;
    }

    /**
     *
     * @return
     * The shipperName
     */
    public String getShipperName() {
        return shipperName;
    }

    /**
     *
     * @param shipperName
     * The shipper_name
     */
    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    /**
     *
     * @return
     * The originId
     */
    public Integer getOriginId() {
        return originId;
    }

    /**
     *
     * @param originId
     * The origin_id
     */
    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    /**
     *
     * @return
     * The destinationId
     */
    public Integer getDestinationId() {
        return destinationId;
    }

    /**
     *
     * @param destinationId
     * The destination_id
     */
    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
    }

    /**
     *
     * @return
     * The weight
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     *
     * @param weight
     * The weight
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     *
     * @return
     * The products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     *
     * @param products
     * The products
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return getShipperName();
    }

    public static Attribute createSelectionInfo(String info) {
        Attribute shipment = new Attribute();
        shipment.setShipperName(info);
        shipment.setShipperId("0");
        shipment.setProducts(new ArrayList<Product>());
        return shipment;
    }
}
