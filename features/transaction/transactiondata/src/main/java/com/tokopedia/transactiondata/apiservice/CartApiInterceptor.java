package com.tokopedia.transactiondata.apiservice;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.constant.ResponseStatus;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Response;

/**
 * @author anggaprasetiyo on 23/04/18.
 */
public class CartApiInterceptor extends TkpdAuthInterceptor {

    private static final String RESPONSE_STATUS_REQUEST_DENIED = "REQUEST_DENIED";

    @Inject
    public CartApiInterceptor(Context context, AbstractionRouter abstractionRouter,
                              String authKey) {
        super(context, abstractionRouter, authKey);
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        String responseError = response.peekBody(512).string();
        int errorCode = response.code();
        if (responseError != null && !responseError.contains(RESPONSE_STATUS_REQUEST_DENIED))
            if (!responseError.isEmpty() && responseError.contains("header")) {
                CartErrorResponse cartErrorResponse = new Gson().fromJson(
                        responseError, CartErrorResponse.class
                );
                if (cartErrorResponse.getCartHeaderResponse() != null) {
                    String message = cartErrorResponse.getCartHeaderResponse().getMessageFormatted();
                    if (message == null || message.isEmpty()) {
                        switch (errorCode) {
                            case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                                message = ErrorNetMessage.MESSAGE_ERROR_SERVER;
                                break;
                            case ResponseStatus.SC_FORBIDDEN:
                                message = ErrorNetMessage.MESSAGE_ERROR_FORBIDDEN;
                                break;
                            case ResponseStatus.SC_REQUEST_TIMEOUT:
                            case ResponseStatus.SC_GATEWAY_TIMEOUT:
                                message = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                                break;
                            default:
                                message = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                                break;
                        }
                    }
                    throw new CartResponseErrorException(
                            errorCode,
                            cartErrorResponse.getCartHeaderResponse().getErrorCode(),
                            message);
                }
            }
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method,
                                               String authKey, String contentTypeHeader) {

        Map<String, String> mapHeader = AuthUtil.getDefaultHeaderMap(
                path,
                strParam,
                method,
                contentTypeHeader != null ? contentTypeHeader : "application/x-www-form-urlencoded",
                authKey,
                "EEE, dd MMM yyyy HH:mm:ss ZZZ", userSession.getUserId());

        mapHeader.put("X-APP-VERSION", GlobalConfig.VERSION_NAME);
        mapHeader.put("Tkpd-UserId", userSession.getUserId());
        mapHeader.put("X-Device", "android");
        mapHeader.put("Tkpd-SessionId", userSession.getDeviceId());
        mapHeader.put("Accounts-Authorization", String.format("%s %s", "Bearer",
                userSession.getAccessToken()));


        return mapHeader;
    }
}
