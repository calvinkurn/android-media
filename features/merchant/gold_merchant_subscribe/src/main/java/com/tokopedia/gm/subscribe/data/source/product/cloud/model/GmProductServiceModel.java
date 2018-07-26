package com.tokopedia.gm.subscribe.data.source.product.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GmProductServiceModel {

    @SerializedName("ProductId")
    @Expose
    private Integer productId;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("Notes")
    @Expose
    private String notes;
    @SerializedName("Price")
    @Expose
    private Integer price;
    @SerializedName("PriceFmt")
    @Expose
    private String priceFmt;
    @SerializedName("NextInv")
    @Expose
    private String nextInv;
    @SerializedName("BestDeal")
    @Expose
    private Integer bestDeal;
    @SerializedName("FreeDays")
    @Expose
    private String freeDays;
    @SerializedName("LastPrice")
    @Expose
    private Integer lastPrice;
    @SerializedName("LastPriceFmt")
    @Expose
    private String lastPriceFmt;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPriceFmt() {
        return priceFmt;
    }

    public void setPriceFmt(String priceFmt) {
        this.priceFmt = priceFmt;
    }

    public String getNextInv() {
        return nextInv;
    }

    public void setNextInv(String nextInv) {
        this.nextInv = nextInv;
    }

    public Integer getBestDeal() {
        return bestDeal;
    }

    public void setBestDeal(Integer bestDeal) {
        this.bestDeal = bestDeal;
    }

    public String getFreeDays() {
        return freeDays;
    }

    public void setFreeDays(String freeDays) {
        this.freeDays = freeDays;
    }

    public Integer getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Integer lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getLastPriceFmt() {
        return lastPriceFmt;
    }

    public void setLastPriceFmt(String lastPriceFmt) {
        this.lastPriceFmt = lastPriceFmt;
    }

}
