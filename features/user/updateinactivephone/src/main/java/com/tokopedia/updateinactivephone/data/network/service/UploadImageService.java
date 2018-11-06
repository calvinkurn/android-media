package com.tokopedia.updateinactivephone.data.network.service;

import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneURL;
import com.tokopedia.updateinactivephone.data.network.api.UploadImageApi;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class UploadImageService extends AuthService<UploadImageApi> {

    @Inject
    public UploadImageService() {

    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(UploadImageApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return UpdateInactivePhoneURL.ACCOUNTS_DOMAIN;
    }

    @Override
    public UploadImageApi getApi() {
        return api;
    }
}