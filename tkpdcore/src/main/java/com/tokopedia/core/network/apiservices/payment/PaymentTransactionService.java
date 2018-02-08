package com.tokopedia.core.network.apiservices.payment;

import com.tokopedia.core.network.apiservices.payment.apis.PaymentTransactionApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by kris on 9/12/17. Tokopedia
 */

public class PaymentTransactionService extends AuthService<PaymentTransactionApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(PaymentTransactionApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.PAYMENT_DOMAIN;
    }

    @Override
    public PaymentTransactionApi getApi() {
        return api;
    }
}
