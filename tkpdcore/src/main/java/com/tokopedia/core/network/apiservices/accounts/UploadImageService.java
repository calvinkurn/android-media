package com.tokopedia.core.network.apiservices.accounts;

import com.tokopedia.core.network.apiservices.accounts.apis.UploadImageApi;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by nisie on 3/10/17.
 */

public class UploadImageService extends AuthService<UploadImageApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(UploadImageApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return "https://accounts.tokopedia.com/";
    }

    @Override
    public UploadImageApi getApi() {
        return api;
    }
}
