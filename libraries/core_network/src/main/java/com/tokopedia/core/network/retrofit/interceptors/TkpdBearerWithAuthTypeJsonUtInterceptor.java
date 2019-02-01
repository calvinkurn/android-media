package com.tokopedia.core.network.retrofit.interceptors;

import android.support.annotation.NonNull;

/**
 * Created by sebastianuskh on 3/13/17.
 */

@Deprecated
public class TkpdBearerWithAuthTypeJsonUtInterceptor extends TkpdBearerWithAuthInterceptor {

    private static final String CONTENT_TYPE_JSON_UT = "application/json; charset=UTF-8";

    @NonNull
    @Override
    protected String getContentType() {
        return CONTENT_TYPE_JSON_UT;
    }
}
