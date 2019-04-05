package com.tokopedia.tkpd.campaign.network;

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

public class CampaignAuthInterceptor extends TkpdAuthInterceptor {


    Context mContext;

    public CampaignAuthInterceptor(Context context, AbstractionRouter abstractionRouter) {
        super(context, abstractionRouter);
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originRequest = chain.request();
        Request.Builder newRequest = chain.request().newBuilder();

        generateHmacAuthRequest(originRequest, newRequest);
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
