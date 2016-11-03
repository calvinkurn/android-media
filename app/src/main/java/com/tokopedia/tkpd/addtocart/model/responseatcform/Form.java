package com.tokopedia.tkpd.addtocart.model.responseatcform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Form {

    @SerializedName("province_list")
    @Expose
    private List<ProvinceList> provinceList = new ArrayList<ProvinceList>();
    @SerializedName("shipment")
    @Expose
    private List<Shipment> shipment = new ArrayList<Shipment>();
    @SerializedName("available_count")
    @Expose
    private Integer availableCount;
    @SerializedName("destination")
    @Expose
    private Destination destination;
    @SerializedName("product_detail")
    @Expose
    private ProductDetail productDetail;
    @SerializedName("user_id")
    @Expose
    private String userId;

    /**
     * @return The provinceList
     */
    public List<ProvinceList> getProvinceList() {
        return provinceList;
    }

    /**
     * @param provinceList The province_list
     */
    public void setProvinceList(List<ProvinceList> provinceList) {
        this.provinceList = provinceList;
    }

    /**
     * @return The shipment
     */
    public List<Shipment> getShipment() {
        return shipment;
    }

    /**
     * @param shipment The shipment
     */
    public void setShipment(List<Shipment> shipment) {
        this.shipment = shipment;
    }

    /**
     * @return The availableCount
     */
    public Integer getAvailableCount() {
        return availableCount;
    }

    /**
     * @param availableCount The available_count
     */
    public void setAvailableCount(Integer availableCount) {
        this.availableCount = availableCount;
    }

    /**
     * @return The destination
     */
    public Destination getDestination() {
        return destination;
    }

    /**
     * @param destination The destination
     */
    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    /**
     * @return The productDetail
     */
    public ProductDetail getProductDetail() {
        return productDetail;
    }

    /**
     * @param productDetail The product_detail
     */
    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

}
