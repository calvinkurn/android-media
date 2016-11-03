package com.tokopedia.tkpd.network.apiservices.transaction;

import com.tokopedia.tkpd.network.apiservices.transaction.apis.TXActApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class TXActService extends AuthService<TXActApi> {
    private static final String TAG = TXActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TXActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Transaction.URL_TX_ACTION;
    }

    @Override
    public TXActApi getApi() {
        return api;
    }
}
