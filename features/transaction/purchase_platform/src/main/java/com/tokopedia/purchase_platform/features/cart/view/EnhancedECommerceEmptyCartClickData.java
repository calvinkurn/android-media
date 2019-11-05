package com.tokopedia.purchase_platform.features.cart.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Irfan Khoirul on 2019-09-03.
 */

public class EnhancedECommerceEmptyCartClickData {
    private static final String KEY_ACTION_FIELD = "actionField";
    private static final String KEY_PRODUCTS = "products";
    private Map<String, Object> data = new HashMap();

    public EnhancedECommerceEmptyCartClickData() {
    }

    public void setActionField(Map<String, Object> actionFieldData) {
        this.data.put("actionField", actionFieldData);
    }

    public void setProducts(List<Map<String, Object>> productsData) {
        this.data.put("products", productsData);
    }

    public Map<String, Object> getData() {
        return this.data;
    }
}
