package com.tokopedia.core.network.apiservices.mojito;

import android.content.Context;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoAuthApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.GlobalAuthService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import retrofit2.Retrofit;

/**
 * @author ricoharisin on 4/15/16.
 */
public class MojitoAuthService extends GlobalAuthService<MojitoAuthApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MojitoAuthApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.MOJITO_DOMAIN;
    }

    @Override
    public MojitoAuthApi getApi() {
        return api;
    }

    @Override
    protected String getKeyAuth() {
        return AuthUtil.KEY.KEY_MOJITO;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        Context context = MainApplication.getAppContext();
        String deviceId = GCMHandler.getRegistrationId(context);

        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientAuth(getKeyAuth(), deviceId))
                .build();
    }
}
