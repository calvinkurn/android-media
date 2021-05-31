package com.tokopedia.common.network.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tokopedia.common.network.data.db.RestDatabase;
import com.tokopedia.common.network.data.source.cloud.api.RestApi;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthenticator;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.user.session.UserSession;

import java.util.List;

import okhttp3.Interceptor;
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

    // region init default retrofit
    public synchronized static void init(@NonNull Context context) {
        if (sRetrofit == null) {
            UserSession userSession = new UserSession(context.getApplicationContext());
            sFingerprintManager = new FingerprintManager(userSession);
            TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
            tkpdOkHttpBuilder.addInterceptor(new TkpdAuthInterceptor(context, (NetworkRouter) context.getApplicationContext(), userSession));
            tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(), userSession));
            tkpdOkHttpBuilder.addAuthenticator(TkpdAuthenticator.Companion.createAuthenticator(context, (NetworkRouter) context.getApplicationContext(), userSession));
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(RestConstant.BASE_URL)
                    .addConverterFactory(new StringResponseConverter())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(tkpdOkHttpBuilder.build()).build();

            sRestDatabase = RestDatabase.getInstance(context);
            sUserSession = new UserSession(context);
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
            throw new RuntimeException("Please call NetworkClient.init() before using common network library");
        }

        return sRestDatabase;
    }

    public static RestApi getApiInterface() {
        if (sRestApi == null) {
            sRestApi = getRetrofit().create(RestApi.class);
        }
        return sRestApi;
    }
    //endregion

    public static UserSession getsUserSession() {
        if (sUserSession != null)
            return sUserSession;
        else
            throw new RuntimeException("Please call NetworkClient.init() to start the network library.");
    }

    public static synchronized FingerprintManager getFingerPrintManager() {
        if (sFingerprintManager == null) {
            throw new RuntimeException("Please call NetworkClient.init() to start the network library.");
        }
        return sFingerprintManager;
    }

    // region init retrofit to support custom interceptor
    public static RestApi getApiInterfaceCustomInterceptor(@NonNull List<Interceptor> interceptors,
                                                    @NonNull Context context) {
        UserSession userSession = new UserSession(context.getApplicationContext());
        TkpdOkHttpBuilder okkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
        if (interceptors != null) {
            okkHttpBuilder.addInterceptor(new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(), userSession));
            for (Interceptor interceptor : interceptors) {
                if (interceptor == null) {
                    continue;
                }
                okkHttpBuilder.addInterceptor(interceptor);
            }
        }

        return new Retrofit.Builder()
                .baseUrl(RestConstant.BASE_URL)
                .addConverterFactory(new StringResponseConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okkHttpBuilder.build()).build().create(RestApi.class);
    }
    //endregion
}
