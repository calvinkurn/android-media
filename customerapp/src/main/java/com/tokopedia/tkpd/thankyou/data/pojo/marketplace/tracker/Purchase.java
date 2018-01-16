package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.tracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by okasurya on 12/12/17.
 */
public class Purchase extends HashMap<String, Object> {
    public void setActionField(Map<String, Object> actionField) {
        this.put("actionField", actionField);
    }

    public void setProducts(List<Product> products) {
        this.put("products", products);
    }
}
