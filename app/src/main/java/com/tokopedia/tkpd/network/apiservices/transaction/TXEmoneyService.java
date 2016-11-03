package com.tokopedia.tkpd.network.apiservices.transaction;

import com.tokopedia.tkpd.network.apiservices.transaction.apis.TXPaymnetEmoneyApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class TXEmoneyService extends AuthService<TXPaymnetEmoneyApi> {
    private static final String TAG = TXEmoneyService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TXPaymnetEmoneyApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Transaction.URL_TX_PAYMENT_EMONEY;
    }

    @Override
    public TXPaymnetEmoneyApi getApi() {
        return api;
    }
}
