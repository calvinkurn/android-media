package com.tokopedia.posapp.data.pojo.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.posapp.data.pojo.base.Header;

/**
 * Created by okasurya on 10/17/17.
 */

public class ProductList {
    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName("data")
    @Expose
    private ProductData data;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public ProductData getData() {
        return data;
    }

    public void setData(ProductData data) {
        this.data = data;
    }
}
