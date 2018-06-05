package com.tokopedia.core.network.apiservices.transaction;

import com.tokopedia.core.network.apiservices.transaction.apis.TXActApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;

import okhttp3.OkHttpClient;
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
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientDefaultAuthWithUserAgent())
                .build();
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
