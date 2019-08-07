package com.tokopedia.core.network.apiservices.etc;

import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.core.network.apiservices.etc.apis.ContactUsApi;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

@Deprecated
public class ContactUsWsService extends AuthService<ContactUsApi> {
    private static final String TAG = ContactUsWsService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ContactUsApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TokopediaUrl.Companion.getInstance().getWS();
    }

    @Override
    public ContactUsApi getApi() {
        return api;
    }
}
