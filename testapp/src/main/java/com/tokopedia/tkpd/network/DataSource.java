package com.tokopedia.tkpd.network;

import com.tokopedia.application.MyApplication;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataSource {
    public static final String MOCK_ADS_ID = "2df9e57a-849d-4259-99ea-673107469eef";
    public static final String MOCK_FINGERPRINT_HASH = "eyJjYXJyaWVyIjoiQW5kcm9pZCIsImN1cnJlbnRfb3MiOiI4LjAuMCIsImRldmljZV9tYW51ZmFjdHVyZXIiOiJHb29nbGUiLCJkZXZpY2VfbW9kZWwiOiJBbmRyb2lkIFNESyBidWlsdCBmb3IgeDg2IiwiZGV2aWNlX25hbWUiOiJBbmRyb2lkIFNESyBidWlsdCBmb3IgeDg2IiwiZGV2aWNlX3N5c3RlbSI6ImFuZHJvaWQiLCJpc19lbXVsYXRvciI6dHJ1ZSwiaXNfamFpbGJyb2tlbl9yb290ZWQiOmZhbHNlLCJpc190YWJsZXQiOmZhbHNlLCJsYW5ndWFnZSI6ImVuX1VTIiwibG9jYXRpb25fbGF0aXR1ZGUiOiItNi4xNzU3OTQiLCJsb2NhdGlvbl9sb25naXR1ZGUiOiIxMDYuODI2NDU3Iiwic2NyZWVuX3Jlc29sdXRpb24iOiIxMDgwLDE3OTQiLCJzc2lkIjoiXCJBbmRyb2lkV2lmaVwiIiwidGltZXpvbmUiOiJHTVQrNyIsInVzZXJfYWdlbnQiOiJEYWx2aWsvMi4xLjAgKExpbnV4OyBVOyBBbmRyb2lkIDguMC4wOyBBbmRyb2lkIFNESyBidWlsdCBmb3IgeDg2IEJ1aWxkL09TUjEuMTcwOTAxLjA0MykifQ==";
    public static final String MOCK_DEVICE_ID = "cx68b1CtPII:APA91bEV_bdZfq9qPB-xHn2z34ccRQ5M8y9c9pfqTbpIy1AlOrJYSFMKzm_GaszoFsYcSeZY-bTUbdccqmW8lwPQVli3B1fCjWnASz5ZePCpkh9iEjaWjaPovAZKZenowuo4GMD68hoR";
//    public static final String ACCOUNT_TOKOPEDIA_URL = "https://accounts-staging.tokopedia.com/";
//    public static final String BASE_WS_DOMAIN = "https://ws-staging.tokopedia.com/";
    public static final String ACCOUNT_TOKOPEDIA_URL = "https://accounts.tokopedia.com/";
    public static final String BASE_WS_DOMAIN = "https://ws.tokopedia.com/";

    private static LoginService loginService;
    private static AccountService accountService;
    private static WSService wsService;
    private static WSLogoutService wsLogoutService;

    public static LoginService getLoginService(MyApplication application) {
        if (loginService == null) {
            final Retrofit.Builder builder = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            UserSession userSession = new UserSession(application);
            OkHttpClient okHttpClient = okHttpClientBuilder
                    .readTimeout(25, TimeUnit.SECONDS)
                    .addInterceptor(new FingerprintInterceptor(generateFingerprintModel(), userSession))
                    .addInterceptor(new BasicInterceptor(application, application, userSession))
                    .addInterceptor(loggingInterceptor)
                    .build();
            Retrofit retrofit = builder.baseUrl(ACCOUNT_TOKOPEDIA_URL).client(okHttpClient).build();
            loginService = retrofit.create(LoginService.class);
        }
        return loginService;
    }

    public static AccountService getAccountService(MyApplication application) {
        if (accountService == null) {
            final Retrofit.Builder builder = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            UserSession userSession = new UserSession(application);
            OkHttpClient okHttpClient = okHttpClientBuilder
                    .readTimeout(25, TimeUnit.SECONDS)
                    .addInterceptor(new FingerprintInterceptor(generateFingerprintModel(), userSession))
                    .addInterceptor(new TkpdOldAuthInterceptor(application, application, userSession))
                    .addInterceptor(new AccountsBearerInterceptor(userSession))
                    .addInterceptor(loggingInterceptor)
                    .build();
            Retrofit retrofit = builder.baseUrl(ACCOUNT_TOKOPEDIA_URL).client(okHttpClient).build();
            accountService = retrofit.create(AccountService.class);
        }
        return accountService;
    }

    public static WSService getWsService(MyApplication application) {
        if (wsService == null) {
            final Retrofit.Builder builder = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            UserSession userSession = new UserSession(application);
            OkHttpClient okHttpClient = okHttpClientBuilder
                    .readTimeout(25, TimeUnit.SECONDS)
                    .addInterceptor(new FingerprintInterceptor(generateFingerprintModel(), userSession))
                    .addInterceptor(new TkpdOldAuthInterceptor(application, application, userSession))
                    .addInterceptor(new AccountsBearerInterceptor(userSession))
                    .addInterceptor(loggingInterceptor)
                    .build();
            Retrofit retrofit = builder.baseUrl(BASE_WS_DOMAIN).client(okHttpClient).build();
            wsService = retrofit.create(WSService.class);
        }
        return wsService;
    }

    public static WSLogoutService getWsLogoutService(MyApplication application) {
        if (wsLogoutService == null) {
            final Retrofit.Builder builder = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            UserSession userSession = new UserSession(application);
            OkHttpClient okHttpClient = okHttpClientBuilder
                    .readTimeout(25, TimeUnit.SECONDS)
                    .addInterceptor(new FingerprintInterceptor(generateFingerprintModel(), userSession))
                    .addInterceptor(new TkpdAuthInterceptor(application, application, userSession))
                    .addInterceptor(loggingInterceptor)
                    .build();
            Retrofit retrofit = builder.baseUrl(BASE_WS_DOMAIN).client(okHttpClient).build();
            wsLogoutService = retrofit.create(WSLogoutService.class);
        }
        return wsLogoutService;
    }


    public static FingerprintModel generateFingerprintModel() {
        FingerprintModel fingerprintModel = new FingerprintModel();
        fingerprintModel.setFingerprintHash(MOCK_FINGERPRINT_HASH);
        fingerprintModel.setAdsId(MOCK_ADS_ID);
        fingerprintModel.setRegistrarionId(MOCK_DEVICE_ID);
        return fingerprintModel;
    }

}
