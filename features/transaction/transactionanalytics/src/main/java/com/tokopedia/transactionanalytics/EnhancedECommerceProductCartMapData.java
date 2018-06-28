package com.tokopedia.transactionanalytics;

import java.util.HashMap;
import java.util.Map;

/**
 * @author anggaprasetiyo on 05/06/18.
 */
public class EnhancedECommerceProductCartMapData {
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

    public static final String DEFAULT_VALUE_NONE_OTHER = "none/other";

    public EnhancedECommerceProductCartMapData() {

    }

    public void setProductName(String name) {
        Product.put(KEY_NAME, name);
    }

    public void setProductID(String id) {
        Product.put(KEY_ID, id);
    }

    public void setPrice(String price) {
        Product.put(KEY_PRICE, price);
    }

    public void setQty(String qty) {
        Product.put(KEY_QTY, qty);
    }

    public void setCategory(String category) {
        Product.put(KEY_CAT, category);
    }

    public void setDimension38(String data) {
        Product.put(KEY_DIMENSION_38, data);
    }

    public void setAttribution(String data) {
        Product.put(KEY_ATTRIBUTION, data);
    }

    public void setDimension40(String data) {
        Product.put(KEY_DIMENSION_40, data);
    }

    public void setListName(String data) {
        Product.put(KEY_LIST, data);
    }

    public void setPosition(String position) {
        Product.put(KEY_POS, position);
    }

    public void setBrand(String brand) {
        Product.put(KEY_BRAND, brand);
    }

    public void setVariant(String variant) {
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
