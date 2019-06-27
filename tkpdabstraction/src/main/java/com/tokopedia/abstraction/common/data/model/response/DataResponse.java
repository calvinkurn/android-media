package com.tokopedia.abstraction.common.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public class DataResponse<T> {

    @SerializedName("header")
    @Expose
    private Header header = new Header();
    @SerializedName(value="data")
    @Expose
    private T data;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
