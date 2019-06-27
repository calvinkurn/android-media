package com.tokopedia.core.network.apiservices.user;

import com.tokopedia.core.network.apiservices.user.apis.InboxReputationApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public class InboxReputationService extends AuthService<InboxReputationApi> {
    private static final String TAG = InboxReputationService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(InboxReputationApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_INBOX_REPUTATION;
    }

    @Override
    public InboxReputationApi getApi() {
        return api;
    }
}
