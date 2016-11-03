package com.tokopedia.tkpd.network.apiservices.user;

import com.tokopedia.tkpd.network.apiservices.user.apis.InboxTalkApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class InboxTalkService extends AuthService<InboxTalkApi> {
    private static final String TAG = InboxTalkService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(InboxTalkApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_INBOX_TALK;
    }

    @Override
    public InboxTalkApi getApi() {
        return api;
    }
}
