package com.tokopedia.core.analytics.nishikino.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricoharisin on 10/13/15.
 */
public class BaseGTMModel {

    protected Map<String, Object> MainObject = new HashMap<>();

    public BaseGTMModel() {

    }

    public void addValue(String key, String value) {
        MainObject.put(key, value);
    }
}
