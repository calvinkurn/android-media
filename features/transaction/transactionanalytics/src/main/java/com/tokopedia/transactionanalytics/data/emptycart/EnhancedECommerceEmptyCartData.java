package com.tokopedia.transactionanalytics.data.emptycart;

import java.util.HashMap;
<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> f6fc85365a36b1ecd77a8fca9183fe71375629c2
import java.util.Map;

/**
 * Created by Irfan Khoirul on 01/10/18.
 */

public class EnhancedECommerceEmptyCartData {

    private static final String KEY_CLICK = "click";
<<<<<<< HEAD
=======
    private static final String KEY_IMPRESSION = "impressions";
>>>>>>> f6fc85365a36b1ecd77a8fca9183fe71375629c2
    private static final String KEY_CURRENCY_CODE = "currencyCode";
    private static final String DATA_CURRENCY_IDR = "IDR";

    private Map<String, Object> data = new HashMap<>();

    public EnhancedECommerceEmptyCartData() {
        data.put(KEY_CURRENCY_CODE, DATA_CURRENCY_IDR);
    }

    public void setClickData(Map<String, Object> clickData) {
        data.put(KEY_CLICK, clickData);
    }

<<<<<<< HEAD
=======
    public void setImpressionData(List<Map<String, Object>> impressionDataList) {
        data.put(KEY_IMPRESSION, impressionDataList);
    }

>>>>>>> f6fc85365a36b1ecd77a8fca9183fe71375629c2
    public Map<String, Object> getData() {
        return data;
    }
}
