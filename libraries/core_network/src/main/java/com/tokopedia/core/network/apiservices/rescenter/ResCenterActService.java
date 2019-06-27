package com.tokopedia.core.network.apiservices.rescenter;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */

@Deprecated
public class ResCenterActService extends AuthService<ResCenterActApi> {
    private static final String TAG = ResCenterActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ResCenterActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.ResCenter.URL_RES_CENTER_ACTION;
    }

    @Override
    public ResCenterActApi getApi() {
        return api;
    }
}
