package com.tokopedia.networklib.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.networklib.data.source.cloud.api.RestApi;
import com.tokopedia.user.session.UserSession;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static Retrofit sRetrofit = null;
    private static RestApi sRestApi = null;

    private RestClient() {

    }

    public synchronized static void init(@NonNull Context context) {
        if (sRetrofit == null) {
            UserSession userSession = new UserSession(context.getApplicationContext());
            sRetrofit = CommonNetwork.createRetrofit(context.getApplicationContext(),
                    "http://tokopedia.com/", (NetworkRouter) context.getApplicationContext(),
                    userSession);
//            sFingerprintManager = new FingerprintManager(userSession);
//
//            FlowManager.initModule(graphqlGeneratedDatabaseHolder.class);

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create();

            TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
            tkpdOkHttpBuilder.addInterceptor(new TkpdAuthInterceptor(context, (NetworkRouter) context.getApplicationContext(), userSession));
            tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(), userSession));

            sRetrofit = new Retrofit.Builder()
                    .baseUrl("http://tokopedia.com/")
                    .addConverterFactory(new StringResponseConverter())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(tkpdOkHttpBuilder.build()).build();

        }
    }

    private static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            throw new RuntimeException("Please call init() before using graphql library");
        }

        return sRetrofit;
    }

    public static RestApi getApiInterface() {
        if (sRestApi == null) {
            sRestApi = getRetrofit().create(RestApi.class);
        }
        return sRestApi;
    }

//    public static synchronized FingerprintManager getFingerPrintManager() {
//        if (sFingerprintManager == null) {
//            throw new RuntimeException("Please call init() before using graphql library");
//        }
//        return sFingerprintManager;
//    }
}
