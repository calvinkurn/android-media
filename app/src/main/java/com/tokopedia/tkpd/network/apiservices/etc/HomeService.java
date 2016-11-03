package com.tokopedia.tkpd.network.apiservices.etc;

import com.tokopedia.tkpd.network.apiservices.etc.apis.HomeApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

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
