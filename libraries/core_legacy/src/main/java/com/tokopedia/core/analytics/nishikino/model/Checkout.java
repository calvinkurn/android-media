package com.tokopedia.core.analytics.nishikino.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  by ricoharisin on 11/20/15.
 */
public class Checkout {

    private Map<String, Object> ActionField = new HashMap<>();
    private String Currency;

    public Checkout() {

    }

    public void setStep(Object step) {
        ActionField.put("step", step);
    }

    public String getStep() {
        return (String) ActionField.get("step");
    }

    public void setCurrency(String currency) {
        this.Currency = currency;
    }
}
