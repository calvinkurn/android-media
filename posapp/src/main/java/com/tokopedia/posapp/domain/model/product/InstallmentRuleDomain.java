package com.tokopedia.posapp.domain.model.product;

/**
 * Created by okasurya on 8/9/17.
 */

public class InstallmentRuleDomain {
    private String minPurchase;
    private String price;

    public String getMinPurchase() {
        return minPurchase;
    }

    public void setMinPurchase(String minPurchase) {
        this.minPurchase = minPurchase;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
