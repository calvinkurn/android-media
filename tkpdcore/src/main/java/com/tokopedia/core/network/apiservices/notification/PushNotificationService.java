package com.tokopedia.core.network.apiservices.notification;

import com.tokopedia.core.network.apiservices.notification.apis.PushNotificationApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BearerService;

import retrofit2.Retrofit;

/**
 * @author  by alvarisi on 12/8/16.
 */

public class PushNotificationService extends BearerService<PushNotificationApi> {

    public PushNotificationService(String mToken) {
        super(mToken);
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
    protected void initApiService(Retrofit retrofit) {
        this.mApi = retrofit.create(PushNotificationApi.class);
    }

    @Override
    public PushNotificationApi getApi() {
        return this.mApi;
    }
}