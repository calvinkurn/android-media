package com.tokopedia.core.analytics.nishikino.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ricoharisin on 11/20/15.
 */
public class ProductDetail extends BaseGTMModel {

    private Map<String, Object> detail = new HashMap<>();
    private Map<String, Object> actionField = new HashMap<>();
    private List<Object> listProduct = new ArrayList<>();

    public ProductDetail() {

    }

    public void addProduct(Map<String, Object> Product) {
        listProduct.add(Product);
    }

    public void setActionFieldList(String list) {
        actionField.put("list", list);
    }

    public Map<String, Object> getDetailMap() {
        try {
            detail.put("products", listProduct);
            detail.put("actionFields", listProduct);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }
}
