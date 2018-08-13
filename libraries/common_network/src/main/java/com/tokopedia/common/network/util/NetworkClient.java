package com.tokopedia.common.network.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.config.CommonNetworkGeneratedDatabaseHolder;
import com.raizlabs.android.dbflow.config.FlowManager;
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
    private static RestApi sRestApiNoInterceptor = null;
    private static Retrofit sRetrofitNoInterceptor = null;
    private static FingerprintManager sFingerprintManager = null;
    private static UserSession sUserSession;

    private NetworkClient() {

    }

    public synchronized static void init(@NonNull Context context) {
        if (sRetrofit == null) {
            UserSession userSession = new UserSession(context.getApplicationContext());
            sFingerprintManager = new FingerprintManager(userSession);
            FlowManager.initModule(CommonNetworkGeneratedDatabaseHolder.class);
            TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
            tkpdOkHttpBuilder.addInterceptor(new TkpdAuthInterceptor(context, (NetworkRouter) context.getApplicationContext(), userSession));
            tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(), userSession));
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(RestConstant.BASE_URL)
                    .addConverterFactory(new StringResponseConverter())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(tkpdOkHttpBuilder.build()).build();
        }
    }

    private static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            throw new RuntimeException("Please call NetworkClient.init() to start the network library.");
        }

        return sRetrofit;
    }

    public static RestApi getApiInterface() {
        if (sRestApi == null) {
            sRestApi = getRetrofit().create(RestApi.class);
        }
        return sRestApi;
    }

    public static RestApi getApiInterfaceWithNoInterceptor(Context context) {
        if (sRestApiNoInterceptor == null) {
            sRestApiNoInterceptor = getRetrofitNoInterceptor(context).create(RestApi.class);
        }
        return sRestApiNoInterceptor;
    }

    public static Retrofit getRetrofitNoInterceptor(Context context) {
        if (sRetrofitNoInterceptor == null) {
            FlowManager.initModule(CommonNetworkGeneratedDatabaseHolder.class);
            TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
            sRetrofitNoInterceptor = new Retrofit.Builder()
                    .baseUrl(RestConstant.BASE_URL)
                    .addConverterFactory(new StringResponseConverter())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(tkpdOkHttpBuilder.build()).build();
        }
        return sRetrofitNoInterceptor;
    }

    public static synchronized FingerprintManager getFingerPrintManager() {
        if (sFingerprintManager == null) {
            throw new RuntimeException("Please call NetworkClient.init() to start the network library.");
        }
        return sFingerprintManager;
    }
}
