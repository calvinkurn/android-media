package com.tokopedia.posapp.product.productlist.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 10/17/17.
 */

public class ProductItem {
    @SerializedName("id")
    @Expose
    private int productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("image_url_700")
    @Expose
    private String imageUrl700;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("shop")
    @Expose
    private ShopDetail shop;
    @SerializedName("condition")
    @Expose
    private int condition;
    @SerializedName("department_id")
    @Expose
    private int departmentId;
    @SerializedName("rating")
    @Expose
    private double rating;
    @SerializedName("count_review")
    @Expose
    private int countReview;
    @SerializedName("original_price")
    @Expose
    private String originalPrice;
    @SerializedName("discount_price")
    @Expose
    private String discountPrice;
    @SerializedName("discount_expired")
    @Expose
    private String discountExpired;
    @SerializedName("discount_percentage")
    @Expose
    private double discountPercentage;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl700() {
        return imageUrl700;
    }

    public void setImageUrl700(String imageUrl700) {
        this.imageUrl700 = imageUrl700;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getCountReview() {
        return countReview;
    }

    public void setCountReview(int countReview) {
        this.countReview = countReview;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDiscountExpired() {
        return discountExpired;
    }

    public void setDiscountExpired(String discountExpired) {
        this.discountExpired = discountExpired;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public ShopDetail getShop() {
        return shop;
    }

    public void setShop(ShopDetail shop) {
        this.shop = shop;
    }
}

