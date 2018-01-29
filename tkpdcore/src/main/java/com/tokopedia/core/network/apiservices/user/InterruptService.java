package com.tokopedia.core.network.apiservices.user;

import com.tokopedia.core.network.apiservices.user.apis.InterruptApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class InterruptService extends AuthService<InterruptApi> {
    private static final String TAG = InterruptService.class.getSimpleName();

    @Inject
    public InterruptService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(InterruptApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_INTERRUPT;
    }

    @Override
    public InterruptApi getApi() {
        return api;
    }
}
