package com.tokopedia.core.network.apiservices.user;

import com.tokopedia.core.network.apiservices.user.apis.InboxResCenterApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @authory Angga.Prasetiyo on 07/12/2015.
 */
public class InboxResCenterService extends AuthService<InboxResCenterApi> {
    private static final String TAG = InboxResCenterService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(InboxResCenterApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_INBOX_RES_CENTER;
    }

    @Override
    public InboxResCenterApi getApi() {
        return api;
    }
}
