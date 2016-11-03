package com.tokopedia.tkpd.network.apiservices.user;

import com.tokopedia.tkpd.network.apiservices.user.apis.InterruptActApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class InterruptActService extends AuthService<InterruptActApi> {
    private static final String TAG = InterruptActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(InterruptActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_INTERRUPT_ACTION;
    }

    @Override
    public InterruptActApi getApi() {
        return api;
    }
}
