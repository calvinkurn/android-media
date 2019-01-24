package com.tokopedia.core.network.apiservices.kero;

import com.tokopedia.core.network.apiservices.kero.apis.KeroApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.retrofit.services.GlobalAuthService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import retrofit2.Retrofit;


/**
 * Created by Herdi_WORK on 19.09.16.
 */
public class KeroAuthService extends GlobalAuthService<KeroApi> {

    private int numberOfRetry = 0;

    public KeroAuthService(int numberOfRetry) {
        this.numberOfRetry = numberOfRetry;
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(KeroApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.KERO_DOMAIN;
    }

    @Override
    public KeroApi getApi() {
        return api;
    }

    @Override
    protected String getKeyAuth() {
        return AuthUtil.KEY.KEY_KEROPPI;
    }

    @Override
    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(45, 45, 45, numberOfRetry);
    }


}
