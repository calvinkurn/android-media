package com.tokopedia.tkpd.network.apiservices.user;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;
import com.tokopedia.tkpd.network.apiservices.user.apis.MSISDNActApi;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class MSISDNActService extends AuthService<MSISDNActApi> {
    private static final String TAG = MSISDNActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MSISDNActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_MSISDN_ACTION;
    }

    @Override
    public MSISDNActApi getApi() {
        return api;
    }
}
