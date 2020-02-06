package com.tokopedia.events.domain.model.scanticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScanResponseInfo {

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("method")
    @Expose
    private String methodName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
