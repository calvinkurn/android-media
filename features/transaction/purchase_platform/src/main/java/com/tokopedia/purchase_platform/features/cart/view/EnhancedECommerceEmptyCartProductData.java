package com.tokopedia.purchase_platform.features.cart.view;

/**
 * Created by Irfan Khoirul on 2019-09-03.
 */

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class EnhancedECommerceEmptyCartProductData {
    private Map<String, Object> product = new HashMap();
    private static final String KEY_NAME = "name";
    private static final String KEY_ID = "id";
    private static final String KEY_PRICE = "price";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_CAT = "category";
    private static final String KEY_VARIANT = "variant";
    private static final String KEY_LIST = "list";
    private static final String KEY_POS = "position";
    public static final String DEFAULT_VALUE_NONE_OTHER = "none / other";

    public EnhancedECommerceEmptyCartProductData() {
    }

    public void setProductName(String name) {
        this.product.put("name", !TextUtils.isEmpty(name) ? name : "none / other");
    }

    public void setProductID(String id) {
        this.product.put("id", !TextUtils.isEmpty(id) ? id : "none / other");
    }

    public void setPrice(String price) {
        this.product.put("price", !TextUtils.isEmpty(price) ? price : "none / other");
    }

    public void setBrand(String brand) {
        this.product.put("brand", !TextUtils.isEmpty(brand) ? brand : "none / other");
    }

    public void setVariant(String variant) {
        this.product.put("variant", !TextUtils.isEmpty(variant) ? variant : "none / other");
    }

    public void setCategory(String category) {
        this.product.put("category", !TextUtils.isEmpty(category) ? category : "none / other");
    }

    public void setPosition(String position) {
        this.product.put("position", !TextUtils.isEmpty(position) ? position : "none / other");
    }

    public void setList(String list) {
        this.product.put("list", !TextUtils.isEmpty(list) ? list : "none / other");
    }

    public Map<String, Object> getProduct() {
        return this.product;
    }
}
