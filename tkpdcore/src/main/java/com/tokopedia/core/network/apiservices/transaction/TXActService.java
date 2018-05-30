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

    private String userAgent;

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TXActApi.class);
    }


    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        Retrofit.Builder builder = RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl);
        OkHttpFactory factory = OkHttpFactory.create();
        factory.addOkHttpRetryPolicy(getOkHttpRetryPolicy());

        OkHttpClient client;
        if (userAgent.isEmpty())
            client = factory.buildClientDefaultAuth();
        else
            client = factory.buildClientDefaultAuthWithCustomUserAgent(userAgent);

        builder.client(client);
        return builder.build();
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
