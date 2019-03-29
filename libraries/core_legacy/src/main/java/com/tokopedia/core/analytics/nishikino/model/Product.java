package com.tokopedia.core.analytics.nishikino.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricoharisin on 9/29/15.
 */
public class Product {

    private Map<String, Object> Product = new HashMap<>();

    public static final String KEY_NAME = "name";
    public static final String KEY_ID = "id";
    public static final String KEY_PRICE = "price";
    public static final String KEY_BRAND = "brand";
    public static final String KEY_CAT = "category";
    public static final String KEY_VARIANT = "variant";
    public static final String KEY_QTY = "quantity";
    public static final String KEY_SHOP_ID = "shop_id";
    public static final String KEY_SHOP_TYPE = "shop_type";
    public static final String KEY_SHOP_NAME = "shop_name";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_CART_ID = "cart_id";
    public static final String KEY_POS = "position";
    public static final String KEY_LIST = "list";
    public static final String KEY_DIMENSION_38 = "dimension38";
    public static final String KEY_DIMENSION_40 = "dimension40";
    public static final String KEY_ATTRIBUTION = "attribution";
    public static final String KEY_COUPON = "coupon";

    public static final String DEFAULT_VALUE_NONE_OTHER = "none/other";

    public Product() {

    }

    public void setProductName(String name) {
        Product.put(KEY_NAME, name);
    }

    public void setProductID(String id) {
        Product.put(KEY_ID, id);
    }

    public void setPrice(Object price) {
        Product.put(KEY_PRICE, price);
    }

    public void setCoupon(String couponCode) {
        Product.put(KEY_COUPON, couponCode);
    }

    public void setQty(Object qty) {
        Product.put(KEY_QTY, qty);
    }

    public void setCategory(Object category) {
        Product.put(KEY_CAT, category);
    }

    public void setDimension38(Object data) {
        Product.put(KEY_DIMENSION_38, data);
    }

    public void setAttribution(Object data){
        Product.put(KEY_ATTRIBUTION, data);
    }

    public void setDimension40(Object data) {
        Product.put(KEY_DIMENSION_40, data);
    }

    public void setListName(Object data) {
        Product.put(KEY_LIST, data);
    }

    public void setPosition(Object position) {
        Product.put(KEY_POS, position);
    }

    public void setBrand(Object brand) {
        Product.put(KEY_BRAND, brand);
    }

    public void setVariant(Object variant) {
        Product.put(KEY_VARIANT, variant);
    }

    public Map<String, Object> getProduct() {
        return Product;
    }

    public void setShopId(String shopId) {
        Product.put(KEY_SHOP_ID, shopId);
    }

    public void setShopType(String shopType) {
        Product.put(KEY_SHOP_TYPE, shopType);
    }

    public void setShopName(String shopName) {
        Product.put(KEY_SHOP_NAME, shopName);
    }

    public void setCategoryId(String categoryId) {
        Product.put(KEY_CATEGORY_ID, categoryId);
    }

    public void setCartId(String cartId) {
        Product.put(KEY_CART_ID, cartId);
    }

}
