package com.tokopedia.transactiondata.apiservice;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Response;

/**
 * @author anggaprasetiyo on 23/04/18.
 */
public class CartApiInterceptor extends TkpdAuthInterceptor {

    private static final String RESPONSE_STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    private static final String CART_ERROR_GLOBAL = "Maaf, terjadi sedikit kendala. Coba ulangi beberapa saat lagi ya";

    @Inject
    public CartApiInterceptor(Context context, NetworkRouter networkRouter, UserSessionInterface userSession, String authKey) {
        super(context, networkRouter, userSession, authKey);
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        String responseError = response.peekBody(512).string();
        if (!responseError.contains(RESPONSE_STATUS_REQUEST_DENIED))
            if (!responseError.isEmpty() && responseError.contains("header")) {
                throw new CartResponseErrorException(CART_ERROR_GLOBAL);
            }
    }
}
