package com.tokopedia.core.network.apiservices.drawer;

import com.tokopedia.core.network.apiservices.drawer.api.DrawerDataApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class DrawerService extends AuthService<DrawerDataApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(DrawerDataApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.HOME_DATA_BASE_URL;
    }

    @Override
    public DrawerDataApi getApi() {
        return api;
    }
}
