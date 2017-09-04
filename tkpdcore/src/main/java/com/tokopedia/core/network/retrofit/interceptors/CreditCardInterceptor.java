package com.tokopedia.core.network.retrofit.interceptors;

import java.util.Map;

/**
 * Created by kris on 8/22/17. Tokopedia
 */

public class CreditCardInterceptor extends TkpdAuthInterceptor{

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        return super.getHeaderMap(path, strParam, method, authKey, "application/json");
    }
}
