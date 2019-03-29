package com.tokopedia.core.gcm.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.core.network.legacy.core.OkHttpFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author by alvarisi on 12/9/16.
 */

public abstract class BearerService<T> extends BaseService<T>{
    protected T mApi;
    private Context context;
    protected String mToken;

    public BearerService(Context context, String mToken) {
        this.context = context;
        this.mToken = mToken;
        initApiService(createRetrofitInstance(getBaseUrl()));
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientBearerAuth(context, getOauthAuthorization()))
                .build();
    }

    public static Retrofit.Builder createRetrofitDefaultConfig(String baseUrl) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    protected abstract String getBaseUrl();

    protected abstract String getOauthAuthorization();

    protected abstract void initApiService(Retrofit retrofit);

    public abstract T getApi();
}
