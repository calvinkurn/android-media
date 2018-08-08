package com.tokopedia.product.manage.item.common.data.source.cloud;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nathaniel on 12/28/2016.
 * use {@link com.tokopedia.abstraction.common.data.model.response.DataResponse} in other package
 */
@Deprecated
public class DataResponse<T> {

    @SerializedName(value="data", alternate={"result"})
    @Expose
    private T data;

    @SerializedName("eof")
    @Expose
    private boolean eof;

    public boolean isEof() {
        return eof;
    }

    public void setEof(boolean eof) {
        this.eof = eof;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
