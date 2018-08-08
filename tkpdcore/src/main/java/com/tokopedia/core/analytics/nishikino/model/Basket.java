package com.tokopedia.core.analytics.nishikino.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ricoharisin on 11/26/15.
 */
public class Basket extends BaseGTMModel {

    public static final int ADD_ACTION = 1;
    public static final int REMOVE_ACTION = 2;

    private Map<String, Object> Basket = new HashMap<>();
    private Map<String, Object> Ecommerce = new HashMap<>();
    public List<Object> ListProduct = new ArrayList<>();
    public String Currency;
    private int Action = 1;

    public Basket() {

    }

    public void setAction(int act) {
        this.Action = act;

    }

    public void setLabel(String label) {
        Basket.put("eventDetails.label", label);
    }

    public void addProduct(Map<String, Object> Product) {
        ListProduct.add(Product);
    }

    public void setCurrency(String currency) {
        this.Currency = currency;
    }

    public void setUserId(String userId) {
        Basket.put("userId", userId);
    }

    public com.tokopedia.core.analytics.nishikino.model.Basket createBasketMap() {
        Basket.put("eventDetails.category", "Ecommerce");
        String actionString = "";
        if (Action == ADD_ACTION) {
            actionString = "add";
            Basket.put("eventDetails.action", "Add to Basket");
        } else {
            actionString = "remove";
            Basket.put("eventDetails.action", "Remove from Basket");
        }

        Map<String, Object> act = new HashMap<>();
        act.put("products", ListProduct);

        Ecommerce.put("currencyCode", Currency);
        Ecommerce.put(actionString, act);

        Basket.put("ecommerce", Ecommerce);

        return this;
    }

    public Map<String, Object> getBasketMap() {
        return Basket;
    }
}
