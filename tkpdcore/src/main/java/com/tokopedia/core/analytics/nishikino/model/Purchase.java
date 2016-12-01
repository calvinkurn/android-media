package com.tokopedia.core.analytics.nishikino.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ricoharisin on 9/29/15.
 */
public class Purchase {

    private Map<String, Object> ActionField = new HashMap<>();
    private Map<String, Object> Purchase = new HashMap<>();
    private List<Object> ListProduct = new ArrayList<>();
    private String Currency;

    public Purchase() {

    }

    public void setTransactionID(Object id) {
        ActionField.put("id", id);
    }

    public void setAffiliation(Object aff) {
        ActionField.put("affiliation", aff);
    }

    public void setRevenue(Object revenue) {
        ActionField.put("revenue", revenue);
    }

    public void setShipping(Object shipping) {
        ActionField.put("shipping", shipping);
    }

    public void setVoucherCode(Object voucherCode) { ActionField.put("coupon", voucherCode); }

    public void addProduct(Map<String, Object> Product) {
        ListProduct.add(Product);
    }

    public List<Object> getListProduct(){
        return ListProduct;
    }

    public void setCurrency(String currency) {
        this.Currency = currency;
    }

    public Map<String, Object> getPurchase() {
        try {
            Purchase.put("actionField", ActionField);
            Purchase.put("products", ListProduct);
            Purchase.put("currencyCode", Currency);
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
        Currency = null;
    }





}
