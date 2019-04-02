package com.tokopedia.gm.statistic.data.source.cloud.model.graph;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPopularProduct {

    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("sold")
    @Expose
    private long sold;
    @SerializedName("view")
    @Expose
    private int view;
    @SerializedName("success_trans")
    @Expose
    private int successTrans;
    @SerializedName("wishlist_count")
    @Expose
    private int wishlistCount;
    @SerializedName("conversion_rate")
    @Expose
    private double conversionRate;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_link")
    @Expose
    private String productLink;
    @SerializedName("image_link")
    @Expose
    private String imageLink;

    /**
     * @return The productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @param productId The ProductId
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * @return The sold
     */
    public long getSold() {
        return sold;
    }

    /**
     * @param sold The Sold
     */
    public void setSold(long sold) {
        this.sold = sold;
    }

    /**
     * @return The view
     */
    public int getView() {
        return view;
    }

    /**
     * @param view The View
     */
    public void setView(int view) {
        this.view = view;
    }

    /**
     * @return The successTrans
     */
    public int getSuccessTrans() {
        return successTrans;
    }

    /**
     * @param successTrans The SuccessTrans
     */
    public void setSuccessTrans(int successTrans) {
        this.successTrans = successTrans;
    }

    /**
     * @return The wishlistCount
     */
    public int getWishlistCount() {
        return wishlistCount;
    }

    /**
     * @param wishlistCount The WishlistCount
     */
    public void setWishlistCount(int wishlistCount) {
        this.wishlistCount = wishlistCount;
    }

    /**
     * @return The conversionRate
     */
    public double getConversionRate() {
        return conversionRate;
    }

    /**
     * @param conversionRate The ConversionRate
     */
    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }

    /**
     * @return The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName The ProductName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return The productLink
     */
    public String getProductLink() {
        return productLink;
    }

    /**
     * @param productLink The ProductLink
     */
    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    /**
     * @return The imageLink
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     * @param imageLink The ImageLink
     */
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

}
