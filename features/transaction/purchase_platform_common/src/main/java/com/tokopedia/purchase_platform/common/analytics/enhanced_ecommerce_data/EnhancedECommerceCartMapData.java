package com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author anggaprasetiyo on 05/06/18.
 */
public class EnhancedECommerceCartMapData {
    public static final String VALUE_CURRENCY_IDR = "IDR";

    public static final String ADD_ACTION = "add";
    public static final String REMOVE_ACTION = "remove";

    private static final String KEY_CURRENCY = "currencyCode";
    private static final String KEY_PRODUCTS = "products";
    private static final String KEY_IMPRESSIONS = "impressions";
    private static final String KEY_CLICK = "click";

    private Map<String, Object> cart = new HashMap<>();
    private Map<String, Object> act = new HashMap<>();
    private List<Object> listProducts = new ArrayList<>();
    private List<Object> listImpressions = new ArrayList<>();

    public void setCurrencyCode(String currencyCode) {
        cart.put(KEY_CURRENCY, currencyCode);
    }

    public void addProduct(Map<String, Object> Product) {
        listProducts.add(Product);
    }

    public void setAction(String action) {
        act.put(KEY_PRODUCTS, listProducts);
        cart.put(action, act);
    }

    public void addImpression(Map<String, Object> Impression) {
        listImpressions.add(Impression);
        cart.put(KEY_IMPRESSIONS, listImpressions);
    }

    public void addClick(Map<String, Object> Click) {
        cart.put(KEY_CLICK, Click);
    }

    public Map<String, Object> getCartMap() {
        return cart;
    }
}
