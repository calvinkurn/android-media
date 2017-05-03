package com.tokopedia.core.category.data.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.category.data.repository.CategoryRepositoryImpl;
import com.tokopedia.core.category.data.source.CategoryVersionDataSource;
import com.tokopedia.core.category.data.source.cache.CategoryVersionCache;
import com.tokopedia.core.category.data.source.cloud.CategoryVersionCloud;
import com.tokopedia.core.category.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sebastianuskh on 3/9/17.
 */

public class CategoryVersioningHelper {
    public static Subscription checkVersionCategory(Context context, final CategoryVersioningHelperListener listener) {
        HadesCategoryApi api = createHadesApi();
        return getVersioningCategoryObservable(context, api)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.doAfterChecking();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        listener.doAfterChecking();
                    }
                });
    }

    private static Observable<Boolean> getVersioningCategoryObservable(Context context, HadesCategoryApi api) {
        return new CategoryRepositoryImpl(new CategoryVersionDataSource(new CategoryVersionCloud(api), new CategoryVersionCache(context)))
                .checkVersion();
    }

    private static HadesCategoryApi createHadesApi() {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.readTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(45L, TimeUnit.SECONDS);
        TkpdAuthInterceptor authInterceptor = new TkpdAuthInterceptor();
        clientBuilder.interceptors().add(authInterceptor);
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.interceptors().add(logInterceptor);
        OkHttpClient client = clientBuilder.build();

        Gson gson = new Gson();

        return createRetrofit(
                TkpdBaseURL.HADES_DOMAIN,
                client,
                new GeneratedHostConverter(),
                new TkpdResponseConverter(),
                new StringResponseConverter(),
                GsonConverterFactory.create(gson),
                RxJavaCallAdapterFactory.create()
        ).create(HadesCategoryApi.class);
    }

    private static Retrofit createRetrofit(String baseUrl,
                                           OkHttpClient client,
                                           GeneratedHostConverter hostConverter,
                                           TkpdResponseConverter tkpdResponseConverter,
                                           StringResponseConverter stringResponseConverter,
                                           GsonConverterFactory gsonConverterFactory,
                                           RxJavaCallAdapterFactory rxJavaCallAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(hostConverter)
                .addConverterFactory(tkpdResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
    }


}
