package com.tokopedia.posapp.product.management.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductItem {
    @SerializedName("product_id")
    @Expose
    private long productId;
    @SerializedName("condition")
    @Expose
    private int condition;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_encoded")
    @Expose
    private String nameEncoded;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("price")
    @Expose
    private ProductPrice price;
    @SerializedName("outlet")
    @Expose
    private OutletProperties outlet;
    @SerializedName("showcase")
    @Expose
    private ProductShowcase showcase;
    @SerializedName("primary_image")
    @Expose
    private ProductPrimaryImage primaryImage;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEncoded() {
        return nameEncoded;
    }

    public void setNameEncoded(String nameEncoded) {
        this.nameEncoded = nameEncoded;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ProductPrice getPrice() {
        return price;
    }

    public void setPrice(ProductPrice price) {
        this.price = price;
    }

    public ProductShowcase getShowcase() {
        return showcase;
    }

    public void setShowcase(ProductShowcase showcase) {
        this.showcase = showcase;
    }

    public ProductPrimaryImage getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(ProductPrimaryImage primaryImage) {
        this.primaryImage = primaryImage;
    }

    public OutletProperties getOutlet() {
        return outlet;
    }

    public void setOutlet(OutletProperties outlet) {
        this.outlet = outlet;
    }
}
