package com.tokopedia.core.gcm.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.core.network.legacy.core.OkHttpFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author Angga.Prasetiyo on 26/11/2015.
 */
public abstract class BaseService<T> {
    private static final String TAG = BaseService.class.getSimpleName();

    protected T api;

    protected abstract void initApiService(Retrofit retrofit);

    protected abstract String getBaseUrl();

    public abstract T getApi();

    public BaseService() {
        initApiService(createRetrofitInstance(getBaseUrl()));
    }

    public BaseService(String overrideUrl) {
        initApiService(createRetrofitInstance(overrideUrl));
    }

    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return null;
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

    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }

}