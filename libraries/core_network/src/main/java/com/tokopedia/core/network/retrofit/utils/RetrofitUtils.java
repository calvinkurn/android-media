package com.tokopedia.core.network.retrofit.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.util.GeneralUtils;
import com.tokopedia.core.util.GlobalConfig;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author m.normansyah on 27/11/2015.
 */

@Deprecated
public class RetrofitUtils {

    private static final int DEFAULT_TIMEOUT = 120;

    public static Retrofit createRetrofit(String url) {
        return createRetrofit(url, null, -1, DEFAULT_TIMEOUT, true);
    }

    private static Retrofit createRetrofit(String url, String urlProxy, int port, int timeout, boolean enableLogging) {
        // Add the interceptor to OkHttpClient
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        if (GlobalConfig.isAllowDebuggingTools() && enableLogging) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(logging);
        }
        if (GeneralUtils.checkNotNull(urlProxy))
            client.proxy(new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(urlProxy, port)));

        client.connectTimeout(timeout, TimeUnit.SECONDS);
        client.readTimeout(timeout, TimeUnit.SECONDS);
        client.writeTimeout(timeout, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client.build())
                .build();
        return retrofit;
    }
}