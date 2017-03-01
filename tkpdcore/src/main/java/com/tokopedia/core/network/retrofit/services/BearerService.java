package com.tokopedia.core.network.retrofit.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.StandardizedInterceptor;
import com.tokopedia.core.util.GlobalConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author by alvarisi on 12/9/16.
 */

public abstract class BearerService<T> {
    protected T mApi;
    protected String mToken;

    private static final String HEADER_X_APP_VERSION = "X-APP-VERSION";


    public BearerService() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", getOauthAuthorization())
                        .header("X-Device", "android-" + GlobalConfig.VERSION_NAME)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header(HEADER_X_APP_VERSION, "android-" + String.valueOf(GlobalConfig.VERSION_NAME))
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        Interceptor authInterceptor = new StandardizedInterceptor();
        httpClientBuilder.interceptors().add(authInterceptor);
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.interceptors().add(logInterceptor);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        Retrofit.Builder retrofitBuilder =
                new Retrofit.Builder()
                        .baseUrl(getBaseUrl())
                        .addConverterFactory(new GeneratedHostConverter())
                        .addConverterFactory(new TkpdResponseConverter())
                        .addConverterFactory(new StringResponseConverter())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        setInterceptorDebug(httpClientBuilder);
        Retrofit retrofit = retrofitBuilder.client(httpClientBuilder.build()).build();
        initApiService(retrofit);
    }

    private void setInterceptorDebug(OkHttpClient.Builder client) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), DeveloperOptions.CHUCK_ENABLED);
            Boolean allowLogOnNotification = cache.getBoolean(DeveloperOptions.IS_CHUCK_ENABLED, false);
            client.addInterceptor(new ChuckInterceptor(MainApplication.getAppContext())
                    .showNotification(allowLogOnNotification));
        }
    }

    protected abstract String getBaseUrl();

    protected abstract String getOauthAuthorization();

    protected abstract void initApiService(Retrofit retrofit);

    public abstract T getApi();
}
