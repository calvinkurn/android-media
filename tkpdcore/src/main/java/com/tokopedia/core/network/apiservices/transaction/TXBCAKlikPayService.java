package com.tokopedia.core.network.apiservices.transaction;

import com.tokopedia.core.network.apiservices.transaction.apis.TXPaymentBCAKlikPayApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class TXBCAKlikPayService extends AuthService<TXPaymentBCAKlikPayApi> {
    private static final String TAG = TXBCAKlikPayService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TXPaymentBCAKlikPayApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Transaction.URL_TX_PAYMENT_BCA_CLIKPAY;
    }

    @Override
    public TXPaymentBCAKlikPayApi getApi() {
        return api;
    }
}
