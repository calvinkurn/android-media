package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.tracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by okasurya on 12/12/17.
 */
public class Product extends HashMap<String, Object> {
    public void setName(String name) {
        this.put("name", name);
    }

    public void setId(String id) {
        this.put("id", id);
    }

    public void setPrice(String price) {
        this.put("price", price);
    }

    public void setBrand(String brand) {
        this.put("brand", brand);
    }

    public void setCategory(String category) {
        this.put("category", category);
    }

    public void setVariant(String variant) {
        this.put("variant", variant);
    }

    public void setQuantity(int quantity) {
        this.put("quantity", quantity);
    }

    public void setCoupon(String coupon) {
        this.put("coupon", coupon);
    }
}
