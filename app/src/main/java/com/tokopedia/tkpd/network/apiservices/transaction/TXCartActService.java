package com.tokopedia.tkpd.network.apiservices.transaction;

import com.tokopedia.tkpd.network.apiservices.transaction.apis.TXCartActApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class TXCartActService extends AuthService<TXCartActApi> {
    private static final String TAG = TXCartActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TXCartActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Transaction.URL_TX_CART_ACTION;
    }

    @Override
    public TXCartActApi getApi() {
        return api;
    }
}
