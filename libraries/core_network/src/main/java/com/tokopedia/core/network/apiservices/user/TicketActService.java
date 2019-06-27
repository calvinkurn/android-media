package com.tokopedia.core.network.apiservices.user;

import com.tokopedia.core.network.apiservices.user.apis.TicketActApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public class TicketActService extends AuthService<TicketActApi> {
    private static final String TAG = TicketActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TicketActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_TICKET_ACTION;
    }

    @Override
    public TicketActApi getApi() {
        return api;
    }
}
