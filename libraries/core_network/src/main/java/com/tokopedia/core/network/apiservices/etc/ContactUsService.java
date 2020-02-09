package com.tokopedia.core.network.apiservices.etc;

import com.tokopedia.core.network.apiservices.etc.apis.ContactUsApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public class ContactUsService extends AuthService<ContactUsApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ContactUsApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.BASE_CONTACT_US + "/";
    }

    @Override
    public ContactUsApi getApi() {
        return api;
    }
}
