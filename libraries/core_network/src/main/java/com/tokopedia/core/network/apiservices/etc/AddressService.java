package com.tokopedia.core.network.apiservices.etc;

import com.tokopedia.core.network.apiservices.etc.apis.AddressApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public class AddressService extends AuthService<AddressApi> {
    private static final String TAG = AddressService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(AddressApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Etc.URL_ADDRESS;
    }

    @Override
    public AddressApi getApi() {
        return api;
    }
}
