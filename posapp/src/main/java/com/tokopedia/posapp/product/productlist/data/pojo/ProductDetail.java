package com.tokopedia.posapp.product.productlist.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okasurya on 4/13/18.
 */

public class ProductDetail {
    @SerializedName("product_id")
    @Expose
    private int id;
    @SerializedName("product_name")
    @Expose
    private String name;
    @SerializedName("product_alias")
    @Expose
    private String alias;
    @SerializedName("product_condition")
    @Expose
    private String condition;
    @SerializedName("product_price")
    @Expose
    private double price;
    @SerializedName("product_local_price")
    @Expose
    private ProductLocalPrice localPrice;
    @SerializedName("product_url")
    @Expose
    private String url;
    @SerializedName("product_category")
    @Expose
    private ProductCategory category;
    @SerializedName("product_etalase")
    @Expose
    private ProductEtalase etalase;
    @SerializedName("product_shop")
    @Expose
    private ProductShop shop;
    @SerializedName("product_picture")
    @Expose
    private List<ProductPicture> pictures;
    @SerializedName("product_sku")
    @Expose
    private String sku;
    @SerializedName("product_gtin")
    @Expose
    private String gtin;
    @SerializedName("product_description")
    @Expose
    private String productDescription;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductLocalPrice getLocalPrice() {
        return localPrice;
    }

    public void setLocalPrice(ProductLocalPrice localPrice) {
        this.localPrice = localPrice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public ProductEtalase getEtalase() {
        return etalase;
    }

    public void setEtalase(ProductEtalase etalase) {
        this.etalase = etalase;
    }

    public ProductShop getShop() {
        return shop;
    }

    public void setShop(ProductShop shop) {
        this.shop = shop;
    }

    public List<ProductPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<ProductPicture> pictures) {
        this.pictures = pictures;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
