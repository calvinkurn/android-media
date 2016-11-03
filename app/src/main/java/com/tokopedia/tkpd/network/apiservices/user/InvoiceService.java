package com.tokopedia.tkpd.network.apiservices.user;

import com.tokopedia.tkpd.network.apiservices.user.apis.InvoiceApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class InvoiceService extends AuthService<InvoiceApi> {
    private static final String TAG = InvoiceService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(InvoiceApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_INVOICE;
    }

    @Override
    public InvoiceApi getApi() {
        return api;
    }
}
