package com.tokopedia.contactus.common.di.network;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.core.util.SessionHandler;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sandeepgoyal on 09/04/18.
 */

public class ContactUsAuthInterceptor extends TkpdAuthInterceptor {


    Context mContext;
    public ContactUsAuthInterceptor(Context context, AbstractionRouter abstractionRouter) {
        super(context, abstractionRouter);
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
                    .addHeader("Authorization", "Bearer " + SessionHandler.getAccessToken())
                    .addHeader("Tkpd-UserId", SessionHandler.getLoginID(mContext));

            final Request finalRequest = newRequest.build();
            Response response = getResponse(chain, finalRequest);

            if (!response.isSuccessful()) {
                throwChainProcessCauseHttpError(response);
            }

            String bodyResponse = response.body().string();
            checkResponse(bodyResponse, response);

            return createNewResponse(response, bodyResponse);
    }
}
