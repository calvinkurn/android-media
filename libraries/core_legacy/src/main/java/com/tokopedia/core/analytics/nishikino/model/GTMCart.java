package com.tokopedia.core.analytics.nishikino.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by herdimac on 6/15/16.
 */
public class GTMCart {

    private static final String KEY_CURRENCY = "currencyCode";

    private Map<String, Object> cart = new HashMap<>();

    public void setCurrencyCode(String currencyCode) {
        cart.put(KEY_CURRENCY, currencyCode);
    }

    public Map<String, Object> getCartMap() {
        return cart;
    }

}
