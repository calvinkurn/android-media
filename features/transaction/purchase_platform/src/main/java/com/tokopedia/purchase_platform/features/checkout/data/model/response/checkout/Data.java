package com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class Data {
    @SerializedName("product_list")
    @Expose
    private List<ProductList> productList = new ArrayList<>();
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;
    @SerializedName("callback_url")
    @Expose
    private String callbackUrl;
    @SerializedName("parameter")
    @Expose
    private Parameter parameter;
    @SerializedName("query_string")
    @Expose
    private String queryString;

    public List<ProductList> getProductList() {
        return productList;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public String getQueryString() {
        return queryString;
    }
}
