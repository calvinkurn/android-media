
package com.tokopedia.digital.categorylist.data.cloud.entity;

import com.google.gson.annotations.SerializedName;

public class HomeCategoryMenuItem {

    @SerializedName("data")
    private Data mData;
    @SerializedName("headers")
    private Headers mHeaders;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public Headers getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Headers headers) {
        mHeaders = headers;
    }

}
