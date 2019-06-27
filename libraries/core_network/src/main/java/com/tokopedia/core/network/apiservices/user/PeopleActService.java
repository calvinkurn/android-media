package com.tokopedia.core.network.apiservices.user;

import com.tokopedia.core.network.apiservices.user.apis.PeopleActApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public class PeopleActService extends AuthService<PeopleActApi> {
    private static final String TAG = PeopleActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(PeopleActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_PEOPLE_ACTION;
    }

    @Override
    public PeopleActApi getApi() {
        return api;
    }
}
