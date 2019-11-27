package com.tokopedia.contactus.common.di.network;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sandeepgoyal on 09/04/18.
 */

public class ContactUsAuthInterceptor extends TkpdAuthInterceptor {


    Context mContext;
    public ContactUsAuthInterceptor(Context context, NetworkRouter networkRouter, UserSessionInterface userSessionInterface) {
        super(context, networkRouter, userSessionInterface);
        mContext = context;
    }
    private static final String HEADER_SESSION_ID = "tkpd-SessionId";

    @Override
    public Response intercept(Chain chain) throws IOException {
            final Request originRequest = chain.request();
            Request.Builder newRequest = chain.request().newBuilder();

            generateHmacAuthRequest(originRequest, newRequest);
            newRequest.addHeader(HEADER_SESSION_ID,userSession.getDeviceId());
            newRequest.removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer " + userSession.getAccessToken())
                    .addHeader("Tkpd-UserId", userSession.getUserId());

            final Request finalRequest = newRequest.build();
            Response response = getResponse(chain, finalRequest);

            if (!response.isSuccessful()) {
                throwChainProcessCauseHttpError(response);
            }
            checkResponse(response);

            return response;
    }
}
