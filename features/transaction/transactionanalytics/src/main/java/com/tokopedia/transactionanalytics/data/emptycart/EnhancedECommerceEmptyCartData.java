package com.tokopedia.transactionanalytics.data.emptycart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Irfan Khoirul on 01/10/18.
 */

public class EnhancedECommerceEmptyCartData {

    private static final String KEY_CLICK = "click";
    private static final String KEY_IMPRESSION = "impressions";
    private static final String KEY_CURRENCY_CODE = "currencyCode";
    private static final String DATA_CURRENCY_IDR = "IDR";

    private Map<String, Object> data = new HashMap<>();

    public EnhancedECommerceEmptyCartData() {
        data.put(KEY_CURRENCY_CODE, DATA_CURRENCY_IDR);
    }

    public void setClickData(Map<String, Object> clickData) {
        data.put(KEY_CLICK, clickData);
    }

    public void setImpressionData(List<Map<String, Object>> impressionDataList) {
        data.put(KEY_IMPRESSION, impressionDataList);
    }

    public Map<String, Object> getData() {
        return data;
    }
}
