package com.tokopedia.core.shopinfo.facades.authservices;


import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by Tkpd_Eka on 12/7/2015.
 */
public class ActionService extends AuthService<ActionApi> {

    private static final String URL_ACTION = "https://ws-staging.tokopedia.com/v4/action/";

    public ActionService(){}

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ActionApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return URL_ACTION;
    }

    @Override
    public ActionApi getApi() {
        return api;
    }
}
