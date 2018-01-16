package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.tracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by okasurya on 12/12/17.
 */
public class Product extends HashMap<String, Object> {
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PRICE = "price";
    private static final String BRAND = "brand";
    private static final String CATEGORY = "category";
    private static final String VARIANT = "variant";
    private static final String QUANTITY = "quantity";
    private static final String COUPON = "coupon";

    public void setName(String name) {
        this.put(NAME, name);
    }

    public void setId(String id) {
        this.put(ID, id);
    }

    public void setPrice(String price) {
        this.put(PRICE, price);
    }

    public void setBrand(String brand) {
        this.put(BRAND, brand);
    }

    public void setCategory(String category) {
        this.put(CATEGORY, category);
    }

    public void setVariant(String variant) {
        this.put(VARIANT, variant);
    }

    public void setQuantity(int quantity) {
        this.put(QUANTITY, quantity);
    }

    public void setCoupon(String coupon) {
        this.put(COUPON, coupon);
    }
}
