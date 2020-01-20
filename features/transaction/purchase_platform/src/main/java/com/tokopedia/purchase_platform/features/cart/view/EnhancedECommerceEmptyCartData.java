package com.tokopedia.purchase_platform.features.cart.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Irfan Khoirul on 2019-09-03.
 */

public class EnhancedECommerceEmptyCartData {
    private static final String KEY_CLICK = "click";
    private static final String KEY_IMPRESSION = "impressions";
    private static final String KEY_CURRENCY_CODE = "currencyCode";
    private static final String DATA_CURRENCY_IDR = "IDR";
    private Map<String, Object> data = new HashMap();

    public EnhancedECommerceEmptyCartData() {
        this.data.put("currencyCode", "IDR");
    }

    public void setClickData(Map<String, Object> clickData) {
        this.data.put("click", clickData);
    }

    public void setImpressionData(List<Map<String, Object>> impressionDataList) {
        this.data.put("impressions", impressionDataList);
    }

    public Map<String, Object> getData() {
        return this.data;
    }
}
