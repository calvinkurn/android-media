package com.tokopedia.sellerapp.daggerModules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.sellerapp.BuildConfig;
import com.tokopedia.sellerapp.gmstat.utils.GMStatConstant;
import com.tokopedia.sellerapp.utils.Constants;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tokopedia.sellerapp.gmstat.utils.GMStatConstant.GMSTAT_TAG;

/**
 * Created by normansyahputa on 8/28/16.
 * 2 november 2016, start developing gmstat network.
 */
@Module
public class NetworkModules {
    public static final String V4_OKHTTP = "V4";

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    @Provides
    @Singleton
    TkpdResponseConverter provideTkpdResponseConverter(){
        return new TkpdResponseConverter();
    }

    @Provides
    @Singleton
    GsonConverterFactory provideConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides @Named(V4_OKHTTP)
    @Singleton
    public OkHttpClient provideOkHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(new TkpdAuthInterceptor());
        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logInterceptor);
        }

        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(
            @Named(V4_OKHTTP) OkHttpClient okHttpClient,
            GsonConverterFactory converterFactory,
            TkpdResponseConverter tkpdResponseConverter){
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(tkpdResponseConverter)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides @Named(GMSTAT_TAG)
    @Singleton
    public OkHttpClient provideGMOkHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(new TkpdAuthInterceptor());
        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logInterceptor);
        }
        return builder.build();

    }

    @Named(GMSTAT_TAG)
    @Provides
    @Singleton
    public Retrofit providGMRetrofit(
            @Named(GMSTAT_TAG) OkHttpClient okHttpClient,
            GsonConverterFactory converterFactory){
        return new Retrofit.Builder()
                .baseUrl(GMStatConstant.baseUrl)
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


}
