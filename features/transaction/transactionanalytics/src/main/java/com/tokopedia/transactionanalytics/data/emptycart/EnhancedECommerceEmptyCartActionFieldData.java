package com.tokopedia.transactionanalytics.data.emptycart;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Irfan Khoirul on 01/10/18.
 */

public class EnhancedECommerceEmptyCartActionFieldData {

    private static final String KEY_LIST = "list";
    public static final String VALUE_SECTION_NAME_WISHLIST = "/cart - wishlist";
    public static final String VALUE_SECTION_NAME_RECENT_VIEW = "/cart - recent view";
    public static final String VALUE_SECTION_NAME_RECOMMENDATION = "/cart - rekomendasi untuk anda";

    private Map<String, Object> data = new HashMap<>();

    public Map<String, Object> getData() {
        return data;
    }

    public void setList(String value) {
        data.put(KEY_LIST, value);
    }
}
