package com.tokopedia.tkpd.thankyou.data.pojo.digital.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Purchase {
    @SerializedName("actionField")
    @Expose
    private ActionField actionField;
    @SerializedName("products")
    @Expose
    private List<Product> products;

    public ActionField getActionField() {
        return actionField;
    }

    public void setActionField(ActionField actionField) {
        this.actionField = actionField;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}