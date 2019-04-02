package com.tokopedia.gm.subscribe.data.source.product.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("Product")
    @Expose
    private List<GmProductServiceModel> product = null;
    @SerializedName("Extend")
    @Expose
    private List<GmProductServiceModel> extend = null;
    @SerializedName("PayMethod")
    @Expose
    private String payMethod;

    public List<GmProductServiceModel> getProduct() {
        return product;
    }

    public void setProduct(List<GmProductServiceModel> product) {
        this.product = product;
    }

    public List<GmProductServiceModel> getExtend() {
        return extend;
    }

    public void setExtend(List<GmProductServiceModel> extend) {
        this.extend = extend;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

}
