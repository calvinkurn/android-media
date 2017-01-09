package com.tokopedia.core.network.apiservices.transaction;

import com.tokopedia.core.network.apiservices.transaction.apis.TokoCashApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.GlobalAuthService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import retrofit2.Retrofit;

/**
 * Created by kris on 1/5/17. Tokopedia
 */

public class TokoCashService extends GlobalAuthService<TokoCashApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TokoCashApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TopCash.GET_WALLET;
    }

    @Override
    public TokoCashApi getApi() {
        return api;
    }

    @Override
    protected String getKeyAuth() {
        return AuthUtil.KEY.KEY_WSV4;
    }
}
