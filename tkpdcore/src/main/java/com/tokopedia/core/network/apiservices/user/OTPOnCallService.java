package com.tokopedia.core.network.apiservices.user;

import com.tokopedia.core.network.apiservices.user.apis.OtpOnCallApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BearerService;

import retrofit2.Retrofit;

/**
 * Created by nisie on 12/22/16.
 */

public class OTPOnCallService extends BearerService<OtpOnCallApi> {
    private static final String TAG = OTPOnCallService.class.getSimpleName();

    public OTPOnCallService(String Oauth) {
        this.mToken = Oauth;
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        mApi = retrofit.create(OtpOnCallApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.ACCOUNTS_DOMAIN;
    }

    @Override
    protected String getOauthAuthorization() {
        return "Bearer " + mToken;
    }

    @Override
    public OtpOnCallApi getApi() {
        return mApi;
    }
}