package com.tokopedia.core.network.apiservices.user;

import com.tokopedia.core.network.apiservices.user.apis.NotificationApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public class NotificationService extends AuthService<NotificationApi> {
    private static final String TAG = NotificationService.class.getSimpleName();

    @Inject
    public NotificationService() {
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(NotificationApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_NOTIFICATION;
    }

    @Override
    public NotificationApi getApi() {
        return api;
    }
}
