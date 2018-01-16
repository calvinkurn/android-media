package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.tracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by okasurya on 12/12/17.
 */
public class Ecommerce extends HashMap<String, Object> {
    private static final String CURRENCY_CODE = "currencyCode";
    private static final String PURCHASE = "purchase";

    public void setCurrencyCode(String currencyCode) {
        this.put(CURRENCY_CODE, currencyCode);
    }

    public void setPurchase(Map<String, Object> purchase) {
        this.put(PURCHASE, purchase);
    }
}
