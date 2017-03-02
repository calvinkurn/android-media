package com.tokopedia.digital.cart.model;

/**
 * Created by Nabilla Sabbaha on 2/28/2017.
 */
public class CartItemDigital {

    private String label;

    private String value;

    public CartItemDigital(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
