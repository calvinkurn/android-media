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
    private static final String KEY_CART_ID = "dimension45";
    private static final String KEY_POS = "position";
    private static final String KEY_LIST = "list";
    private static final String KEY_DIMENSION_38 = "dimension38";
    private static final String KEY_DIMENSION_40 = "dimension40";
    private static final String KEY_DIMENSION_45 = "dimension45";
    private static final String KEY_DIMENSION_54 = "dimension54";
    private static final String KEY_DIMENSION_52 = "dimension52";
    private static final String KEY_DIMENSION_53 = "dimension53";
    private static final String KEY_DIMENSION_57 = "dimension57";
    private static final String KEY_DIMENSION_59 = "dimension59";
    private static final String KEY_DIMENSION_77 = "dimension77";
    private static final String KEY_DIMENSION_80 = "dimension80";
    private static final String KEY_DIMENSION_12 = "dimension12";
    private static final String KEY_ATTRIBUTION = "attribution";
    private static final String KEY_WAREHOUSE_ID = "dimension56";
    private static final String KEY_PRODUCT_WEIGHT = "dimension48";
    private static final String KEY_PROMO_CODE = "dimension49";
    private static final String KEY_PROMO_DETAILS = "dimension59";
    private static final String KEY_BUYER_ADDRESS_ID = "dimension11";
    private static final String KEY_SHIPPING_DURATION = "dimension16";
    private static final String KEY_COURIER = "dimension14";
    private static final String KEY_SHIPPING_PRICE = "dimension12";
    private static final String KEY_COD_FLAG = "dimension10";
    private static final String KEY_TOKOPEDIA_CORNER_FLAG = "dimension57";
    private static final String KEY_IS_FULFILLMENT = "dimension58";
    private static final String KEY_PICTURE = "picture";
    private static final String KEY_URL = "url";

    public static final String DEFAULT_VALUE_NONE_OTHER = "none/other";

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

    public void setDimension54(Boolean isFulfill) {
        String data = isFulfill ? "tokopedia" : "regular";
        Product.put(KEY_DIMENSION_54, !TextUtils.isEmpty(data) ? data : DEFAULT_VALUE_NONE_OTHER);
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
        Product.put(KEY_BRAND, brand != null ? brand : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setVariant(String variant) {
        Product.put(KEY_VARIANT, variant != null ? variant : DEFAULT_VALUE_NONE_OTHER);
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
        Product.put(KEY_CATEGORY_ID, categoryId != null ? categoryId : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setCartId(String cartId) {
        Product.put(KEY_CART_ID, !TextUtils.isEmpty(cartId) ? cartId : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setDimension45(String cartId) {
        Product.put(KEY_DIMENSION_45, !TextUtils.isEmpty(cartId) ? cartId : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setDimension12(String shippingCourierPrice) {
        Product.put(KEY_DIMENSION_12, !TextUtils.isEmpty(shippingCourierPrice) ? shippingCourierPrice : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setWarehouseId(String warehouseId) {
        Product.put(KEY_WAREHOUSE_ID, !TextUtils.isEmpty(warehouseId) ? warehouseId : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setProductWeight(String productWeight) {
        Product.put(KEY_PRODUCT_WEIGHT, !TextUtils.isEmpty(productWeight) ? productWeight : DEFAULT_VALUE_NONE_OTHER);
    }

    public void setPromoCode(String promoCodes) {
        Product.put(KEY_PROMO_CODE, !TextUtils.isEmpty(promoCodes) ? promoCodes : "");
    }

    public void setPromoDetails(String promoDetails) {
        Product.put(KEY_PROMO_DETAILS, !TextUtils.isEmpty(promoDetails) ? promoDetails : "");
    }

    public void setBuyerAddressId(String buyerAddressId) {
        Product.put(KEY_BUYER_ADDRESS_ID, !TextUtils.isEmpty(buyerAddressId) ? buyerAddressId : "");
    }

    public void setShippingDuration(String shippingDuration) {
        Product.put(KEY_SHIPPING_DURATION, !TextUtils.isEmpty(shippingDuration) ? shippingDuration : "");
    }

    public void setCourier(String courier) {
        Product.put(KEY_COURIER, !TextUtils.isEmpty(courier) ? courier : "");
    }

    public void setShippingPrice(String shippingPrice) {
        Product.put(KEY_SHIPPING_PRICE, !TextUtils.isEmpty(shippingPrice) ? shippingPrice : "");
    }

    public void setCodFlag(String codFlag) {
        Product.put(KEY_COD_FLAG, !TextUtils.isEmpty(codFlag) ? codFlag : "");
    }

    public void setTokopediaCornerFlag(String tokopediaCornerFlag) {
        Product.put(KEY_TOKOPEDIA_CORNER_FLAG, !TextUtils.isEmpty(tokopediaCornerFlag) ? tokopediaCornerFlag : "");
    }

    public void setIsFulfillment(String isFulfillment) {
        Product.put(KEY_IS_FULFILLMENT, !TextUtils.isEmpty(isFulfillment) ? isFulfillment : "");
    }

    public void setPicture(String picture) {
        Product.put(KEY_PICTURE, !TextUtils.isEmpty(picture) ? picture : "");
    }

    public void setUrl(String url) {
        Product.put(KEY_URL, !TextUtils.isEmpty(url) ? url : "");
    }

    public void setDimension52(String shopId) {
        Product.put(KEY_DIMENSION_52, !TextUtils.isEmpty(shopId) ? shopId : "");
    }

    public void setDimension53(boolean isDiscountPrice) {
        Product.put(KEY_DIMENSION_53, String.valueOf(isDiscountPrice));
    }

    public void setDimension57(String shopName) {
        Product.put(KEY_DIMENSION_57, !TextUtils.isEmpty(shopName) ? shopName : "");
    }

    public void setDimension59(String shopType) {
        Product.put(KEY_DIMENSION_59, !TextUtils.isEmpty(shopType) ? shopType : "");
    }

    public void setDimension77(String cartId) {
        Product.put(KEY_DIMENSION_77, !TextUtils.isEmpty(cartId) ? cartId : "");
    }

}
