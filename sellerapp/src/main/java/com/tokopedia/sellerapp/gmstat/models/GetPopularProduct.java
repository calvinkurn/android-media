package com.tokopedia.sellerapp.gmstat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class GetPopularProduct {

    @SerializedName("ProductId")
    @Expose
    private Integer productId;
    @SerializedName("Sold")
    @Expose
    private Integer sold;
    @SerializedName("View")
    @Expose
    private Integer view;
    @SerializedName("SuccessTrans")
    @Expose
    private Integer successTrans;
    @SerializedName("WishlistCount")
    @Expose
    private Integer wishlistCount;
    @SerializedName("ConversionRate")
    @Expose
    private Integer conversionRate;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("ProductLink")
    @Expose
    private String productLink;
    @SerializedName("ImageLink")
    @Expose
    private String imageLink;

    /**
     *
     * @return
     * The productId
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     *
     * @param productId
     * The ProductId
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     *
     * @return
     * The sold
     */
    public Integer getSold() {
        return sold;
    }

    /**
     *
     * @param sold
     * The Sold
     */
    public void setSold(Integer sold) {
        this.sold = sold;
    }

    /**
     *
     * @return
     * The view
     */
    public Integer getView() {
        return view;
    }

    /**
     *
     * @param view
     * The View
     */
    public void setView(Integer view) {
        this.view = view;
    }

    /**
     *
     * @return
     * The successTrans
     */
    public Integer getSuccessTrans() {
        return successTrans;
    }

    /**
     *
     * @param successTrans
     * The SuccessTrans
     */
    public void setSuccessTrans(Integer successTrans) {
        this.successTrans = successTrans;
    }

    /**
     *
     * @return
     * The wishlistCount
     */
    public Integer getWishlistCount() {
        return wishlistCount;
    }

    /**
     *
     * @param wishlistCount
     * The WishlistCount
     */
    public void setWishlistCount(Integer wishlistCount) {
        this.wishlistCount = wishlistCount;
    }

    /**
     *
     * @return
     * The conversionRate
     */
    public Integer getConversionRate() {
        return conversionRate;
    }

    /**
     *
     * @param conversionRate
     * The ConversionRate
     */
    public void setConversionRate(Integer conversionRate) {
        this.conversionRate = conversionRate;
    }

    /**
     *
     * @return
     * The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     *
     * @param productName
     * The ProductName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     *
     * @return
     * The productLink
     */
    public String getProductLink() {
        return productLink;
    }

    /**
     *
     * @param productLink
     * The ProductLink
     */
    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    /**
     *
     * @return
     * The imageLink
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     *
     * @param imageLink
     * The ImageLink
     */
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

}