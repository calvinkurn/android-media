package com.tokopedia.tkpd.network.apiservices.user;

import com.tokopedia.tkpd.network.apiservices.user.apis.MessageActApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class MessageActService extends AuthService<MessageActApi> {
    private static final String TAG = MessageActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MessageActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_MESSAGE_ACTION;
    }

    @Override
    public MessageActApi getApi() {
        return api;
    }
}
