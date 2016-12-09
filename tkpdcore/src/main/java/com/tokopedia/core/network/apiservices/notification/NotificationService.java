package com.tokopedia.core.network.apiservices.notification;

import com.tokopedia.core.network.apiservices.notification.apis.NotificationApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;
import com.tokopedia.core.network.retrofit.services.BearerService;

import retrofit2.Retrofit;

/**
 * @author  by alvarisi on 12/8/16.
 */

public class NotificationService extends BearerService<NotificationApi> {
    public NotificationService(String Oauth){
        this.mToken = Oauth;
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.BASE_DOMAIN;
    }

    @Override
    protected String getOauthAuthorization() {
        return "Bearer " + mToken;
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        this.mApi = retrofit.create(NotificationApi.class);
    }

    @Override
    public NotificationApi getApi() {
        return this.mApi;
    }
}