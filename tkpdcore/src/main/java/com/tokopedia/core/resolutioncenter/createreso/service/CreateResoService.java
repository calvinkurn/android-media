package com.tokopedia.core.resolutioncenter.createreso.service;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResoService extends AuthService<CreateResoApi> {
    public CreateResoService() {
        super();
    }

    public CreateResoService(String overrideUrl) {
        super(overrideUrl);
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(CreateResoApi.class);
    }


    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.ResCenterV5.URL_RES_CENTER_ACTION_CREATE;
    }

    @Override
    public CreateResoApi getApi() {
        return api;
    }
}
