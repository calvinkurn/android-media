package com.tokopedia.core.instoped;

import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;


/**
 * Created by Tkpd_Eka on 4/6/2016.
 */
public class InstopedService extends BaseService<InstopedApi> {

    public static String INSTAGRAM_API = "https://api.instagram.com/";

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(InstopedApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return INSTAGRAM_API;
    }

    @Override
    public InstopedApi getApi() {
        return api;
    }
}
