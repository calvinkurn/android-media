package com.tokopedia.core.network.apiservices.user;

import com.tokopedia.core.network.apiservices.user.apis.ApiaryApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by nisie on 12/22/16.
 */

public class ApiaryService extends AuthService<ApiaryApi> {
    private static final String TAG = ApiaryService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ApiaryApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_OTP_WITH_CALL;
    }

    @Override
    public ApiaryApi getApi() {
        return api;
    }
}