package com.tokopedia.core.analytics.nishikino.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ricoharisin on 11/26/15.
 */
public class Impression extends BaseGTMModel {

    private Map<String, Object> Impression = new HashMap<>();
    private Map<String, Object> ActionField = new HashMap<>();
    public List<Object> ListProduct = new ArrayList<>();
    public String Currency;

    public Impression() {

    }

    public void setList(String list) {
        ActionField.put("list", list);
    }

    public void addProduct(Map<String, Object> Product) {
        ListProduct.add(Product);
    }

    public void setCurrency(String currency) {
        this.Currency = currency;
    }

    public Map<String, Object> getImpressionMap() {
        try {
            Impression.put("impressions", ListProduct);
            Impression.put("currencyCode", Currency);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Impression;
    }
}
