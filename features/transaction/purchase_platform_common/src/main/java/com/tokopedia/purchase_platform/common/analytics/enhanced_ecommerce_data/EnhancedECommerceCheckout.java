package com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author anggaprasetiyo on 31/07/18.
 */
public class EnhancedECommerceCheckout {

    private static final String KEY_PRODUCT = "products";
    private static final String KEY_CURRENCY_CODE = "currencyCode";
    private static final String KEY_ACTION_FIELD = "actionField";

    public static final String KEY_CHECKOUT = "checkout";

    private Map<String, Object> checkoutMap = new HashMap<>();
    private List<Object> listProducts = new ArrayList<>();

    public void addProduct(Map<String, Object> Product) {
        listProducts.add(Product);
    }

    public void setCurrencyCode(String currencyCode) {
        checkoutMap.put(KEY_CURRENCY_CODE, currencyCode);
    }

    public void setActionField(Map<String, String> actionFieldMap) {
        checkoutMap.put(KEY_ACTION_FIELD, actionFieldMap);
    }

    public Map<String, Object> getCheckoutMap() {
        checkoutMap.put(KEY_PRODUCT, listProducts);
        return checkoutMap;
    }
}
