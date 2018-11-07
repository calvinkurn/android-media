package com.tokopedia.transactionanalytics.data.emptycart;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Irfan Khoirul on 01/10/18.
 */

public class EnhancedECommerceEmptyCartProductData {

    private Map<String, Object> product = new HashMap<>();

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
        product.put(KEY_NAME, !TextUtils.isEmpty(name) ? name : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setProductID(String id) {
        product.put(KEY_ID, !TextUtils.isEmpty(id) ? id : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setPrice(String price) {
        product.put(KEY_PRICE, !TextUtils.isEmpty(price) ? price : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setBrand(String brand) {
        product.put(KEY_BRAND, !TextUtils.isEmpty(brand) ? brand : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setVariant(String variant) {
        product.put(KEY_VARIANT, !TextUtils.isEmpty(variant) ? variant : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setCategory(String category) {
        product.put(KEY_CAT, !TextUtils.isEmpty(category) ? category : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setPosition(String position) {
        product.put(KEY_POS, !TextUtils.isEmpty(position) ? position : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setList(String list) {
        product.put(KEY_LIST, !TextUtils.isEmpty(list) ? list : DEFAULT_VALUE_NONE_OTHER);
    }

    public Map<String, Object> getProduct() {
        return product;
    }
}
