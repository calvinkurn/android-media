package com.tokopedia.core.network.apiservices.upload;

import com.tokopedia.core.network.apiservices.upload.apis.UploadImageApi;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author anggaprasetiyo on 8/11/16.
 */

@Deprecated
public class UploadImageService extends AuthService<UploadImageApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(UploadImageApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return "https://ws.tokopedia.com/v4/product/";
    }

    @Override
    public UploadImageApi getApi() {
        return api;
    }
}
