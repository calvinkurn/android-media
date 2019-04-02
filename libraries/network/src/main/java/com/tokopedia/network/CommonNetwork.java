package com.tokopedia.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.cpm.CharacterPerMinuteInterface;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.user.session.UserSession;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ricoharisin .
 */

public class CommonNetwork {

    /**
     method to create retrofit object for general purpose
     */
    public static Retrofit createRetrofit(Context context, String baseUrl, NetworkRouter networkRouter, UserSession userSession, CharacterPerMinuteInterface characterPerMinuteInterface) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
        tkpdOkHttpBuilder.addInterceptor(new TkpdAuthInterceptor(context, networkRouter, userSession));
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor(networkRouter, userSession, characterPerMinuteInterface));

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.create())
                .client(tkpdOkHttpBuilder.build()).build();
    }

    /**
    method to create retrofit object for general purpose
     */
    public static Retrofit createRetrofit(Context context, String baseUrl, NetworkRouter networkRouter, UserSession userSession) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
        tkpdOkHttpBuilder.addInterceptor(new TkpdAuthInterceptor(context, networkRouter, userSession));
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor(networkRouter, userSession));

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.create())
                .client(tkpdOkHttpBuilder.build()).build();
    }

    /**
     method to create retrofit object if want to use dagger
     */
    public static Retrofit createRetrofit(String baseUrl, TkpdOkHttpBuilder tkpdOkHttpBuilder,
                                          TkpdAuthInterceptor tkpdAuthInterceptor, FingerprintInterceptor fingerprintInterceptor,
                                          StringResponseConverter stringResponseConverter, GsonBuilder gsonBuilder) {
        Gson gson = gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        tkpdOkHttpBuilder.addInterceptor(tkpdAuthInterceptor);
        tkpdOkHttpBuilder.addInterceptor(fingerprintInterceptor);

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.create())
                .client(tkpdOkHttpBuilder.build()).build();
    }
}
