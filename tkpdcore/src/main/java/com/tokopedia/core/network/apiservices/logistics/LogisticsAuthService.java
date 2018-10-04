package com.tokopedia.core.network.apiservices.logistics;

import com.tokopedia.core.network.apiservices.logistics.apis.LogisticsApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by sachinbansal on 4/6/18.
 */

public class LogisticsAuthService extends AuthService<LogisticsApi> {


    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(LogisticsApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.HOME_DATA_BASE_URL;
    }

    @Override
    public LogisticsApi getApi() {
        return api;
    }
}
