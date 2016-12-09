package com.tokopedia.core.network.apiservices.transaction;

import com.tokopedia.core.network.apiservices.transaction.apis.TXPaymentSprintAsiaApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class TXSprintAsiaService extends AuthService<TXPaymentSprintAsiaApi> {
    private static final String TAG = TXSprintAsiaService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TXPaymentSprintAsiaApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Transaction.URL_TX_PAYMENT_SPRINT_ASIA;
    }

    @Override
    public TXPaymentSprintAsiaApi getApi() {
        return api;
    }
}
