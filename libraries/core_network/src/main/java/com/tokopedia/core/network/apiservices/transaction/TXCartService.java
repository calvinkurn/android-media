package com.tokopedia.core.network.apiservices.transaction;

import com.tokopedia.core.network.apiservices.transaction.apis.TXCartApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */

@Deprecated
public class TXCartService extends AuthService<TXCartApi> {
    private static final String TAG = TXCartService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TXCartApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Transaction.URL_TX_CART;
    }

    @Override
    public TXCartApi getApi() {
        return api;
    }
}
