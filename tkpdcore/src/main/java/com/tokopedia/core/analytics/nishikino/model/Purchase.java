package com.tokopedia.core.analytics.nishikino.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ricoharisin on 9/29/15.
 */
public class Purchase {
    public static final String PAYMENT_ID = "payment_id";
    public static final String PAYMENT_STATUS = "payment_status";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String SHOP_ID = "shop_id";
    public static final String LOGISTIC_TYPE = "logistic_type";
    public static final String ECOMMERCE = "ecommerce";
    public static final String USER_ID = "userId";
    public static final String PURCHASE = "purchase";
    public static final String DEFAULT_CURRENCY_VALUE = "IDR";

    public static final String ID = "id";

    public static final String NAME = "name";
    public static final String REVENUE_KEY = "revenue";
    public static final String SHIPPING_KEY = "shipping";

    private Map<String, Object> ActionField = new HashMap<>();
    private Map<String, Object> Purchase = new HashMap<>();
    private List<Object> ListProduct = new ArrayList<>();
    private String currency;
    private String shopId;
    private String paymentId;
    private String paymentType;
    private String logisticType;
    private String userId;
    private String event;
    private String paymentStatus;
    private String eventCategory;
    private String eventLabel;
    private String shopType;

    public Purchase() {

    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getLogisticType() {
        return logisticType;
    }

    public void setLogisticType(String logisticType) {
        this.logisticType = logisticType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventCategory() { return eventCategory; }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventLabel() { return eventLabel; }

    public void setEventLabel(String eventLabel) {
        this.eventLabel = eventLabel;
    }

    public void setTransactionID(Object id) {
        ActionField.put("id", id);
    }

    public Object getTransactionID() {
       return ActionField.get("id");
    }

    public void setAffiliation(Object aff) {
        ActionField.put("affiliation", aff);
    }

    public void setRevenue(Object revenue) {
        ActionField.put(REVENUE_KEY, revenue);
    }

    public void setShipping(Object shipping) {
        ActionField.put(SHIPPING_KEY, shipping);
    }

    public Object getRevenue() {
       return ActionField.get(REVENUE_KEY);
    }

    public Object getShipping() {
       return ActionField.get(SHIPPING_KEY);
    }

    public void setVoucherCode(Object voucherCode) { ActionField.put("coupon", voucherCode); }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void addProduct(Map<String, Object> Product) {
        ListProduct.add(Product);
    }

    public List<Object> getListProduct(){
        return ListProduct;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Map<String, Object> getPurchase() {
        try {
            Purchase.put("actionField", ActionField);
            Purchase.put("products", ListProduct);
            Purchase.put("currencyCode", currency);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Purchase;
    }

    public void clearPurchase() {
        setAffiliation(null);
        setRevenue(null);
        setShipping(null);
        setTransactionID(null);
        setVoucherCode(null);
        ListProduct = null;
        currency = null;
    }
}