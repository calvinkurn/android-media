package com.tokopedia.core.network.retrofit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.util.GlobalConfig;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * @author m.normansyah on 27/11/2015.
 */
public class RetrofitUtils {

    public static final String NETWORK_FORBIDDEN_ERROR = "silahkan login kembali";
    public static final String NETWORK_TIMEOUT_ERROR = "Network Timeout Error!";
    public static final int RETRY_COUNT = 3;
    public static final int DEFAULT_TIMEOUT = 120;

    public static Retrofit createRetrofitWIthProxy(String urlProxy, int port) {
        return createRetrofit(TkpdBaseURL.BASE_DOMAIN, urlProxy, port, DEFAULT_TIMEOUT, false);
    }

    public static Retrofit createRetrofit() {
        return createRetrofit(TkpdBaseURL.BASE_DOMAIN);
    }

    public static Retrofit createRetrofitV4(String url) {
        String baseUrl = url;
        if (baseUrl.startsWith("https://ws") & BuildConfig.DEBUG) {
            String path = baseUrl.substring(baseUrl.indexOf("v4"));
            SharedPreferences pref = MainApplication.getAppContext()
                    .getSharedPreferences("DOMAIN_WS_4", Context.MODE_PRIVATE);
            baseUrl = pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN) + path;
        }
        return createRetrofit(baseUrl);
    }

    public static Retrofit createRetrofit(String url) {
        return createRetrofit(url, null, -1, DEFAULT_TIMEOUT, true);
    }

    public static Retrofit createRetrofitWithLogging(String url) {
        return createRetrofit(url, null, -1, DEFAULT_TIMEOUT, true);
    }

    public static Retrofit createRetrofit(String url, String urlProxy, int port, int timeout, boolean enableLogging) {
        // Add the interceptor to OkHttpClient
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        if (GlobalConfig.isAllowDebuggingTools() && enableLogging) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(logging);
        }
        if (checkNotNull(urlProxy))
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

    public static boolean isTimeout(String errorMessage) {
        return errorMessage.equals(NETWORK_TIMEOUT_ERROR);
    }

    public static boolean isSessionInvalid(String errorMessage) {
        return errorMessage.contains(NETWORK_FORBIDDEN_ERROR);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static class DefaultErrorHandler extends ErrorHandler {
        public DefaultErrorHandler(int errorCode) {
            super(new ErrorListener() {
                @Override
                public void onUnknown() {
                    throw new RuntimeException("Network Unknown Error!");
                }

                @Override
                public void onTimeout() {
                    throw new RuntimeException(NETWORK_TIMEOUT_ERROR);
                }

                /**
                 * Server unknown problem
                 */
                @Override
                public void onServerError() {
                    throw new RuntimeException("Network Internal Server Error!");
                }

                /**
                 * Server unknown problem
                 */
                @Override
                public void onBadRequest() {
                    throw new RuntimeException("Network Bad Request Error!");
                }

                /**
                 * HMAC Problem
                 */
                @Override
                public void onForbidden() {
                    throw new RuntimeException(NETWORK_FORBIDDEN_ERROR);
                }
            }, errorCode);
        }


    }

}