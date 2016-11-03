package com.tokopedia.tkpd.session.service;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.BaseService;
import com.tokopedia.tkpd.session.api.RegisterApi;

import retrofit2.Retrofit;

/**
 * Created by m.normansyah on 1/25/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class RegisterService extends BaseService<RegisterApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(RegisterApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_REGISTER;
    }

    @Override
    public RegisterApi getApi() {
        return api;
    }
}
