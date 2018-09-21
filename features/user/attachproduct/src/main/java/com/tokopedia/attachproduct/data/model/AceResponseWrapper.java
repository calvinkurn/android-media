package com.tokopedia.attachproduct.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 08/08/18.
 */
public class AceResponseWrapper {
    @SerializedName("data")
    @Expose
    AttachProductAPIResponseWrapper data;

    public AttachProductAPIResponseWrapper getData() {
        return data;
    }

    public void setData(AttachProductAPIResponseWrapper data) {
        this.data = data;
    }
}
