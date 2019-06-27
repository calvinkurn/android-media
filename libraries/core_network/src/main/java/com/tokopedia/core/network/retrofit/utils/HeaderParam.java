package com.tokopedia.core.network.retrofit.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Angga.Prasetiyo on 25/11/2015.
 */

@Deprecated
public class HeaderParam {
    private static final String TAG = HeaderParam.class.getSimpleName();

    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
