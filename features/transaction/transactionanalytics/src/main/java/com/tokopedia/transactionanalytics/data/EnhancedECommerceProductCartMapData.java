package com.tokopedia.transactionanalytics.data;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author anggaprasetiyo on 05/06/18.
 */
public class EnhancedECommerceProductCartMapData {
    private Map<String, Object> Product = new HashMap<>();

    public static final String SHOP_TYPE_REGULER = "reguler";
    public static final String SHOP_TYPE_OFFICIAL_STORE = "official_store";
    public static final String SHOP_TYPE_GOLD_MERCHANT = "gold_merchant";

    private static final String KEY_NAME = "name";
    private static final String KEY_ID = "id";
    private static final String KEY_PRICE = "price";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_CAT = "category";
    private static final String KEY_VARIANT = "variant";
    private static final String KEY_QTY = "quantity";
    private static final String KEY_SHOP_ID = "shop_id";
    private static final String KEY_SHOP_TYPE = "shop_type";
    private static final String KEY_SHOP_NAME = "shop_name";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_CART_ID = "cart_id";
    private static final String KEY_POS = "position";
    private static final String KEY_LIST = "list";
    private static final String KEY_DIMENSION_38 = "dimension38";
    private static final String KEY_DIMENSION_40 = "dimension40";
    private static final String KEY_DIMENSION_45 = "dimension45";
    private static final String KEY_DIMENSION_80 = "dimension80";
    private static final String KEY_ATTRIBUTION = "attribution";

    public static final String DEFAULT_VALUE_NONE_OTHER = "none / other";

    public EnhancedECommerceProductCartMapData() {

    }

    public void setProductName(String name) {
        Product.put(KEY_NAME, !TextUtils.isEmpty(name) ? name : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setProductID(String id) {
        Product.put(KEY_ID, !TextUtils.isEmpty(id) ? id : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setPrice(String price) {
        Product.put(KEY_PRICE, !TextUtils.isEmpty(price) ? price : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setQty(int qty) {
        String qtyString = String.valueOf(qty);
        Product.put(KEY_QTY, !TextUtils.isEmpty(qtyString) ? qtyString : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setCategory(String category) {
        Product.put(KEY_CAT, !TextUtils.isEmpty(category) ? category : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setAttribution(String data) {
        Product.put(KEY_ATTRIBUTION, !TextUtils.isEmpty(data) ? data : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setDimension38(String data) {
        Product.put(KEY_DIMENSION_38, !TextUtils.isEmpty(data) ? data : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setDimension80(String data) {
        Product.put(KEY_DIMENSION_80, !TextUtils.isEmpty(data) ? data : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setListName(String data) {
        Product.put(KEY_LIST, !TextUtils.isEmpty(data) ? data : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setDimension40(String data) {
        Product.put(KEY_DIMENSION_40, !TextUtils.isEmpty(data) ? data : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setPosition(String position) {
        Product.put(KEY_POS, !TextUtils.isEmpty(position) ? position : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setBrand(String brand) {
        Product.put(KEY_BRAND, !TextUtils.isEmpty(brand) ? brand : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setVariant(String variant) {
        Product.put(KEY_VARIANT, !TextUtils.isEmpty(variant) ? variant : DEFAULT_VALUE_NONE_OTHER);
    }

    public Map<String, Object> getProduct() {
        return Product;
    }

    public void setShopId(String shopId) {
        Product.put(KEY_SHOP_ID, !TextUtils.isEmpty(shopId) ? shopId : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setShopType(String shopType) {
        Product.put(KEY_SHOP_TYPE, !TextUtils.isEmpty(shopType) ? shopType : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setShopName(String shopName) {
        Product.put(KEY_SHOP_NAME, !TextUtils.isEmpty(shopName) ? shopName : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setCategoryId(String categoryId) {
        Product.put(KEY_CATEGORY_ID, !TextUtils.isEmpty(categoryId) ? categoryId : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setCartId(String cartId) {
        Product.put(KEY_CART_ID, !TextUtils.isEmpty(cartId) ? cartId : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setDimension45(String cartId) {
        Product.put(KEY_DIMENSION_45, !TextUtils.isEmpty(cartId) ? cartId : DEFAULT_VALUE_NONE_OTHER);
    }
}
