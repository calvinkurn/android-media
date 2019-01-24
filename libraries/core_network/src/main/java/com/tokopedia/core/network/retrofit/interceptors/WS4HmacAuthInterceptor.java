package com.tokopedia.core.network.retrofit.interceptors;

import java.io.IOException;

import okhttp3.Response;

/**
 * @author anggaprasetiyo on 3/4/17.
 */

public class WS4HmacAuthInterceptor extends AuthHmacInterceptor {

    public WS4HmacAuthInterceptor(String authKey) {
        super(authKey);
    }

    public WS4HmacAuthInterceptor() {
        super();
    }

    @Override
    protected boolean isAutoRetry() {
        return true;
    }

    @Override
    protected void throwChainProcessCauseHttpError(Response response) throws IOException {

    }

}
