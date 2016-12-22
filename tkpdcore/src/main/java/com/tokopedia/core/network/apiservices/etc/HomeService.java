package com.tokopedia.core.network.apiservices.etc;

import com.tokopedia.core.network.apiservices.etc.apis.home.HomeApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class HomeService extends AuthService<HomeApi> {
    private static final String TAG = HomeService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(HomeApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Etc.URL_HOME;
    }

    @Override
    public HomeApi getApi() {
        return api;
    }
}
