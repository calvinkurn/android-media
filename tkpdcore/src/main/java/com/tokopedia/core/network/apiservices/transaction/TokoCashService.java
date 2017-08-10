package com.tokopedia.core.network.apiservices.transaction;

import com.tokopedia.core.network.apiservices.transaction.apis.TokoCashApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BearerService;

import retrofit2.Retrofit;

/**
 * Created by kris on 1/5/17. Tokopedia
 */

public class TokoCashService extends BearerService<TokoCashApi> {

    public TokoCashService(String mToken) {
        super(mToken);
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        this.mApi = retrofit.create(TokoCashApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TopCash.GET_WALLET;
    }

    @Override
    protected String getOauthAuthorization() {
        return "Bearer " + mToken;
    }

    @Override
    public TokoCashApi getApi() {
        initApiService(createRetrofitInstance(getBaseUrl()));
        return this.mApi;
    }

    public void setToken(String accessToken) {
        mToken = accessToken;
    }

}
