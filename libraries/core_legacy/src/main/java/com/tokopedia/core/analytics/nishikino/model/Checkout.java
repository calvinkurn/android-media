package com.tokopedia.core.analytics.nishikino.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  by ricoharisin on 11/20/15.
 */
public class Checkout extends BaseGTMModel {

    private Map<String, Object> ActionField = new HashMap<>();
    private Map<String, Object> Checkout = new HashMap<>();
    private List<Object> ListProduct = new ArrayList<>();
    private String Currency;

    public Checkout() {

    }

    public void setStep(Object step) {
        ActionField.put("step", step);
    }

    public String getStep() {
        return (String) ActionField.get("step");
    }

    public void setCheckoutOption(String option){
        ActionField.put("option", option);
    }

    public void addProduct(Map<String, Object> Product) {
        ListProduct.add(Product);
    }

    public void setCurrency(String currency) {
        this.Currency = currency;
    }

    public Map<String, Object> getCheckoutMap() {
        try {
            Checkout.put("actionField", ActionField);
            Checkout.put("products", ListProduct);
            Checkout.put("currencyCode", Currency);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Checkout;
    }

    public Map<String, Object> getCheckoutMapEvent() {
        Log.i("Tag Manager", "UA-9801603-15: Send Checkout Event New");
        try {
            Checkout.put("actionField", ActionField);
            Checkout.put("products", ListProduct);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Checkout;
    }
}
