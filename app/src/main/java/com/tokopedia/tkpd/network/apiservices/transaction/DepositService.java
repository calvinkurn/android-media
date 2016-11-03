package com.tokopedia.tkpd.network.apiservices.transaction;

import com.tokopedia.tkpd.network.apiservices.transaction.apis.DepositApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class DepositService extends AuthService<DepositApi> {
    private static final String TAG = DepositService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(DepositApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Transaction.URL_DEPOSIT;
    }

    @Override
    public DepositApi getApi() {
        return api;
    }
}
