package com.tokopedia.transactionanalytics.data.emptycart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Irfan Khoirul on 01/10/18.
 */

public class EnhancedECommerceEmptyCartClickData {

    private static final String KEY_ACTION_FIELD = "actionField";
    private static final String KEY_PRODUCTS = "products";

    private Map<String, Object> data = new HashMap<>();

    public void setActionField(Map<String, Object> actionFieldData) {
        data.put(KEY_ACTION_FIELD, actionFieldData);
    }

    public void setProducts(List<Map<String, Object>> productsData) {
        data.put(KEY_PRODUCTS, productsData);
    }

    public Map<String, Object> getData() {
        return data;
    }
}
