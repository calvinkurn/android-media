package com.tokopedia.common.network.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.common.network.data.db.RestDatabase;
import com.tokopedia.common.network.data.source.cloud.api.RestApi;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.user.session.UserSession;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class NetworkClient {
    private static Retrofit sRetrofit = null;
    private static RestApi sRestApi = null;
    private static FingerprintManager sFingerprintManager = null;
    private static UserSession sUserSession;
    private static RestDatabase sRestDatabase;

    private NetworkClient() {

    }

    public synchronized static void init(@NonNull Context context) {
        if (sRetrofit == null) {
            UserSession userSession = new UserSession(context.getApplicationContext());
            sFingerprintManager = new FingerprintManager(userSession);
            TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
            tkpdOkHttpBuilder.addInterceptor(new TkpdAuthInterceptor(context, (NetworkRouter) context.getApplicationContext(), userSession));
            tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(), userSession));
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(RestConstant.BASE_URL)
                    .addConverterFactory(new StringResponseConverter())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(tkpdOkHttpBuilder.build()).build();

            sRestDatabase = RestDatabase.getInstance(context);
        }
    }

    private static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            throw new RuntimeException("Please call NetworkClient.init() to start the network library.");
        }

        return sRetrofit;
    }

    public static RestDatabase getRestDatabase() {
        if (sRestDatabase == null) {
            throw new RuntimeException("Please call init() before using common network library");
        }

        return sRestDatabase;
    }

    public static RestApi getApiInterface() {
        if (sRestApi == null) {
            sRestApi = getRetrofit().create(RestApi.class);
        }
        return sRestApi;
    }


    public static synchronized FingerprintManager getFingerPrintManager() {
        if (sFingerprintManager == null) {
            throw new RuntimeException("Please call NetworkClient.init() to start the network library.");
        }
        return sFingerprintManager;
    }
}
