package com.tokopedia.tkpd.network.apiservices.transaction;

import com.tokopedia.tkpd.network.apiservices.transaction.apis.TXPaymentCCBCAApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class TXCCBCAService extends AuthService<TXPaymentCCBCAApi> {
    private static final String TAG = TXCCBCAService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TXPaymentCCBCAApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Transaction.URL_TX_PAYMENT_CC_BC;
    }

    @Override
    public TXPaymentCCBCAApi getApi() {
        return api;
    }
}
