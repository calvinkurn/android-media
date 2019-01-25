
package com.tokopedia.core.shopinfo.models.productmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class Product {

    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("product_image_full")
    @Expose
    private String productImageFull;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_wholesale")
    @Expose
    private int productWholesale;
    @SerializedName("shop_location")
    @Expose
    private String shopLocation;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("shop_gold_status")
    @Expose
    private int shopGoldStatus;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("rate")
    @Expose
    private int rate;
    @SerializedName("product_sold_count")
    @Expose
    private int productSoldCount;
    @SerializedName("product_review_count")
    @Expose
    private int productReviewCount;
    @SerializedName("product_talk_count")
    @Expose
    private int productTalkCount;
    @SerializedName("is_owner")
    @Expose
    private int isOwner;
    @SerializedName("shop_lucky")
    @Expose
    private String shopLucky;
    @SerializedName("condition")
    @Expose
    private int condition;
    @SerializedName("preorder")
    @Expose
    private int preorder;
    @SerializedName("product_preorder")
    @Expose
    private int productPreorder;
    @SerializedName("badges")
    @Expose
    private List<Badge> badges = new ArrayList<Badge>();
    @SerializedName("labels")
    @Expose
    private List<Label> labels = new ArrayList<Label>();

    /**
     * 
     * @return
     *     The productUrl
     */
    public String getProductUrl() {
        return productUrl;
    }

    /**
     * 
     * @param productUrl
     *     The product_url
     */
    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    /**
     * 
     * @return
     *     The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 
     * @param productName
     *     The product_name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 
     * @return
     *     The productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     * 
     * @param productId
     *     The product_id
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * 
     * @return
     *     The productImageFull
     */
    public String getProductImageFull() {
        return productImageFull;
    }

    /**
     * 
     * @param productImageFull
     *     The product_image_full
     */
    public void setProductImageFull(String productImageFull) {
        this.productImageFull = productImageFull;
    }

    /**
     * 
     * @return
     *     The productImage
     */
    public String getProductImage() {
        return productImage;
    }

    /**
     * 
     * @param productImage
     *     The product_image
     */
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    /**
     * 
     * @return
     *     The productPrice
     */
    public String getProductPrice() {
        return productPrice;
    }

    /**
     * 
     * @param productPrice
     *     The product_price
     */
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * 
     * @return
     *     The productWholesale
     */
    public int getProductWholesale() {
        return productWholesale;
    }

    /**
     * 
     * @param productWholesale
     *     The product_wholesale
     */
    public void setProductWholesale(int productWholesale) {
        this.productWholesale = productWholesale;
    }

    /**
     * 
     * @return
     *     The shopLocation
     */
    public String getShopLocation() {
        return shopLocation;
    }

    /**
     * 
     * @param shopLocation
     *     The shop_location
     */
    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    /**
     * 
     * @return
     *     The shopUrl
     */
    public String getShopUrl() {
        return shopUrl;
    }

    /**
     * 
     * @param shopUrl
     *     The shop_url
     */
    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    /**
     * 
     * @return
     *     The shopGoldStatus
     */
    public int getShopGoldStatus() {
        return shopGoldStatus;
    }

    /**
     * 
     * @param shopGoldStatus
     *     The shop_gold_status
     */
    public void setShopGoldStatus(int shopGoldStatus) {
        this.shopGoldStatus = shopGoldStatus;
    }

    /**
     * 
     * @return
     *     The shopName
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * 
     * @param shopName
     *     The shop_name
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * 
     * @return
     *     The shopId
     */
    public int getShopId() {
        return shopId;
    }

    /**
     * 
     * @param shopId
     *     The shop_id
     */
    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    /**
     * 
     * @return
     *     The rate
     */
    public int getRate() {
        return rate;
    }

    /**
     * 
     * @param rate
     *     The rate
     */
    public void setRate(int rate) {
        this.rate = rate;
    }

    /**
     * 
     * @return
     *     The productSoldCount
     */
    public int getProductSoldCount() {
        return productSoldCount;
    }

    /**
     * 
     * @param productSoldCount
     *     The product_sold_count
     */
    public void setProductSoldCount(int productSoldCount) {
        this.productSoldCount = productSoldCount;
    }

    /**
     * 
     * @return
     *     The productReviewCount
     */
    public int getProductReviewCount() {
        return productReviewCount;
    }

    /**
     * 
     * @param productReviewCount
     *     The product_review_count
     */
    public void setProductReviewCount(int productReviewCount) {
        this.productReviewCount = productReviewCount;
    }

    /**
     * 
     * @return
     *     The productTalkCount
     */
    public int getProductTalkCount() {
        return productTalkCount;
    }

    /**
     * 
     * @param productTalkCount
     *     The product_talk_count
     */
    public void setProductTalkCount(int productTalkCount) {
        this.productTalkCount = productTalkCount;
    }

    /**
     * 
     * @return
     *     The isOwner
     */
    public int getIsOwner() {
        return isOwner;
    }

    /**
     * 
     * @param isOwner
     *     The is_owner
     */
    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    /**
     * 
     * @return
     *     The shopLucky
     */
    public String getShopLucky() {
        return shopLucky;
    }

    /**
     * 
     * @param shopLucky
     *     The shop_lucky
     */
    public void setShopLucky(String shopLucky) {
        this.shopLucky = shopLucky;
    }

    /**
     * 
     * @return
     *     The condition
     */
    public int getCondition() {
        return condition;
    }

    /**
     * 
     * @param condition
     *     The condition
     */
    public void setCondition(int condition) {
        this.condition = condition;
    }

    /**
     * 
     * @return
     *     The preorder
     */
    public int getPreorder() {
        return preorder;
    }

    /**
     * 
     * @param preorder
     *     The preorder
     */
    public void setPreorder(int preorder) {
        this.preorder = preorder;
    }

    /**
     * 
     * @return
     *     The productPreorder
     */
    public int getProductPreorder() {
        return productPreorder;
    }

    /**
     * 
     * @param productPreorder
     *     The product_preorder
     */
    public void setProductPreorder(int productPreorder) {
        this.productPreorder = productPreorder;
    }

    /**
     * 
     * @return
     *     The badges
     */
    public List<Badge> getBadges() {
        return badges;
    }

    /**
     * 
     * @param badges
     *     The badges
     */
    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    /**
     * 
     * @return
     *     The labels
     */
    public List<Label> getLabels() {
        return labels;
    }

    /**
     * 
     * @param labels
     *     The labels
     */
    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

}
