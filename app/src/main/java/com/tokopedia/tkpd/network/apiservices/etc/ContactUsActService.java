package com.tokopedia.tkpd.network.apiservices.etc;

import com.tokopedia.tkpd.network.apiservices.etc.apis.ContactUsActApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by nisie on 8/15/16.
 */
public class ContactUsActService extends AuthService<ContactUsActApi> {
    private static final String TAG = ContactUsService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ContactUsActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Etc.URL_CONTACT_US_ACTION;
    }

    @Override
    public ContactUsActApi getApi() {
        return api;
    }
}
