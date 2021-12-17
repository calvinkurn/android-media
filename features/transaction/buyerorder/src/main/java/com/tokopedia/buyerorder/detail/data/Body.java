package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Body implements Serializable {

    @SerializedName("body")
    @Expose
    private String Body;

    @SerializedName("appURL")
    @Expose
    private String AppURL;

    @SerializedName("method")
    @Expose
    private String Method;

    public String getBody() {
        return Body;
    }

    public String getAppURL() {
        return AppURL;
    }

    public String getMethod() {
        return Method;
    }

    @Override
    public String toString() {
        return "ClassPojo [Body = " + Body + ", AppURL = " + AppURL + ", Method = " + Method + "]";
    }
}
