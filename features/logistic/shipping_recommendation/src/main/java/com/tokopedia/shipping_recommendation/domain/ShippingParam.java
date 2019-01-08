package com.tokopedia.shipping_recommendation.domain;

/**
 * Created by Irfan Khoirul on 08/01/19.
 */

public class ShippingParam {
    private String originDistrictId;
    private String originPostalCode;
    private String originLatitude;
    private String originLongitude;
    private String destinationDistrictId;
    private String destinationPostalCode;
    private String destinationLatitude;
    private String destinationLongitude;
    private double weightInGrams;
    private String shopId;
    private String token;
    private String ut;
    private int insurance;
    private int productInsurance;
    private int orderValue;
    private String categoryIds;

    public String getOriginDistrictId() {
        return originDistrictId;
    }

    public void setOriginDistrictId(String originDistrictId) {
        this.originDistrictId = originDistrictId;
    }

    public String getOriginPostalCode() {
        return originPostalCode;
    }

    public void setOriginPostalCode(String originPostalCode) {
        this.originPostalCode = originPostalCode;
    }

    public String getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(String originLatitude) {
        this.originLatitude = originLatitude;
    }

    public String getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(String originLongitude) {
        this.originLongitude = originLongitude;
    }

    public String getDestinationDistrictId() {
        return destinationDistrictId;
    }

    public void setDestinationDistrictId(String destinationDistrictId) {
        this.destinationDistrictId = destinationDistrictId;
    }

    public String getDestinationPostalCode() {
        return destinationPostalCode;
    }

    public void setDestinationPostalCode(String destinationPostalCode) {
        this.destinationPostalCode = destinationPostalCode;
    }

    public String getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(String destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public String getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(String destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public double getWeightInGrams() {
        return weightInGrams;
    }

    public void setWeightInGrams(double weightInGrams) {
        this.weightInGrams = weightInGrams;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public int getProductInsurance() {
        return productInsurance;
    }

    public void setProductInsurance(int productInsurance) {
        this.productInsurance = productInsurance;
    }

    public int getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }
}
