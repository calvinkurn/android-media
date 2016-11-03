package com.tokopedia.tkpd.network.apiservices.clover;

import com.tokopedia.tkpd.network.apiservices.clover.apis.CloverApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Nisie on 4/6/16.
 */
public class CloverService extends AuthService<CloverApi> {
    private static final String TAG = CloverService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(CloverApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Transaction.URL_DEPOSIT_CLOVER;
    }

    @Override
    public CloverApi getApi() {
        return api;
    }
}
