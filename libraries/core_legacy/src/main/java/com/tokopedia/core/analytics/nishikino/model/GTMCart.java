package com.tokopedia.core.analytics.nishikino.model;

import com.tkpd.library.utils.legacy.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by herdimac on 6/15/16.
 */
public class GTMCart {

    public static final String ADD_ACTION = "add";
    public static final String REMOVE_ACTION = "remove";

    private static final String KEY_CURRENCY = "currencyCode";
    private static final String KEY_PRODUCTS = "products";

    private Map<String, Object> cart = new HashMap<>();
    private Map<String, Object> act = new HashMap<>();
    private List<Object> listProducts = new ArrayList<>();

    public void setCurrencyCode(String currencyCode) {
        cart.put(KEY_CURRENCY, currencyCode);
    }

    public void addProduct(Map<String, Object> Product) {
        listProducts.add(Product);
    }

    public void setAddAction(String action) {
        act.put(KEY_PRODUCTS, listProducts);
        cart.put(action, act);
        CommonUtils.dumper("GAv4 action cart " + cart.size() + " product size " + listProducts.size());
    }

    public Map<String, Object> getCartMap() {
        return cart;
    }

}
