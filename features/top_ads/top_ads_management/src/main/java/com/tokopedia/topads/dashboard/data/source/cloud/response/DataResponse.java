package com.tokopedia.topads.dashboard.data.source.cloud.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
