package com.tokopedia.core.network.apiservices.notification;

import com.tokopedia.core.network.apiservices.notification.apis.NotificationApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author  by alvarisi on 12/8/16.
 */

public class NotificationService extends BaseService<NotificationApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(NotificationApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.BASE_DOMAIN;
    }

    @Override
    public NotificationApi getApi() {
        return api;
    }
}
