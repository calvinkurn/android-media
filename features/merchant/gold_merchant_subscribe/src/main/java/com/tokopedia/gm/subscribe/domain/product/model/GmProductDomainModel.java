package com.tokopedia.gm.subscribe.domain.product.model;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GmProductDomainModel {
    private Integer productId;
    private String price;
    private boolean bestDeal;
    private String nextInv;
    private String freeDays;
    private String lastPrice;
    private String name;
    private String notes;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isBestDeal() {
        return bestDeal;
    }

    public void setBestDeal(boolean bestDeal) {
        this.bestDeal = bestDeal;
    }

    public String getNextInv() {
        return nextInv;
    }

    public void setNextInv(String nextInv) {
        this.nextInv = nextInv;
    }

    public String getFreeDays() {
        return freeDays;
    }

    public void setFreeDays(String freeDays) {
        this.freeDays = freeDays;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
