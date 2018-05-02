package com.tokopedia.checkout.view.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.checkout.data.apiservice.CartApi;
import com.tokopedia.checkout.data.apiservice.CartApiInterceptor;
import com.tokopedia.checkout.data.apiservice.CartResponseConverter;
import com.tokopedia.checkout.data.repository.CartRepository;
import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.data.repository.ITopPayRepository;
import com.tokopedia.checkout.data.repository.TopPayRepository;
import com.tokopedia.checkout.view.di.qualifier.CartQualifier;
import com.tokopedia.core.network.apiservices.transaction.TXActService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.TkpdOkHttpBuilder;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author anggaprasetiyo on 29/01/18.
 */
@Module
public class DataModule {

    @Provides
    @CartQualifier
    CartApiInterceptor getCartApiInterceptor(@ApplicationContext Context context,
                                             UserSession userSession,
                                             AbstractionRouter abstractionRouter) {
        return new CartApiInterceptor(context, abstractionRouter, userSession, TkpdBaseURL.Cart.HMAC_KEY);
    }

    @Provides
    @CartQualifier
    public OkHttpClient provideOkHttpClient(@CartQualifier CartApiInterceptor cartApiInterceptor) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient.Builder builder = OkHttpFactory.create()
                .addOkHttpRetryPolicy(OkHttpRetryPolicy.createdOkHttpNoAutoRetryPolicy())
                .getClientBuilder();
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(builder);
        tkpdOkHttpBuilder.addInterceptor(loggingInterceptor);
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor());
        tkpdOkHttpBuilder.addInterceptor(cartApiInterceptor);
        tkpdOkHttpBuilder.setOkHttpRetryPolicy(OkHttpRetryPolicy.createdOkHttpNoAutoRetryPolicy());
        tkpdOkHttpBuilder.addDebugInterceptor();
        return tkpdOkHttpBuilder.build();
    }

    @Provides
    @CartQualifier
    public Retrofit provideCartRetrofit(@CartQualifier OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.BASE_API_DOMAIN)
                .addConverterFactory(CartResponseConverter.create())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @CartQualifier
    public CartApi provideCartApi(@CartQualifier Retrofit retrofit) {
        return retrofit.create(CartApi.class);
    }

    @Provides
    ICartRepository provideICartRepository(@CartQualifier CartApi cartApi) {
        return new CartRepository(cartApi);
    }

    @Provides
    ITopPayRepository provideITopPayRepository(@CartQualifier CartApi cartApi) {
        return new TopPayRepository(new TXActService());
    }
}
