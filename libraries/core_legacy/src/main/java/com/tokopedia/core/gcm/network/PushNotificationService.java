package com.tokopedia.core.gcm.network;

import android.content.Context;

import retrofit2.Retrofit;

/**
 * @author  by alvarisi on 12/8/16.
 */

public class PushNotificationService extends BearerService<PushNotificationApi> {

    public static String ACCOUNTS_DOMAIN = "https://accounts.tokopedia.com/";

    public PushNotificationService(Context context, String mToken) {
        super(context, mToken);
    }

    @Override
    protected String getBaseUrl() {
        return ACCOUNTS_DOMAIN;
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