
package com.tokopedia.payment.setting.add.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiInfo {

    @SerializedName("host")
    @Expose
    private String host;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("headers")
    @Expose
    private Headers headers;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

}
