package com.tokopedia.checkout.view.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.checkout.data.ConstantApiUrl;
import com.tokopedia.checkout.data.apiservice.CartApi;
import com.tokopedia.checkout.data.apiservice.CartApiInterceptor;
import com.tokopedia.checkout.data.apiservice.CartResponseConverter;
import com.tokopedia.checkout.data.apiservice.TxActApi;
import com.tokopedia.checkout.data.repository.CartRepository;
import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.data.repository.ITopPayRepository;
import com.tokopedia.logisticdata.data.repository.RatesRepository;
import com.tokopedia.checkout.data.repository.TopPayRepository;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.di.qualifier.CartApiInterceptorQualifier;
import com.tokopedia.checkout.view.di.qualifier.CartApiOkHttpClientQualifier;
import com.tokopedia.checkout.view.di.qualifier.CartApiRetrofitQualifier;
import com.tokopedia.checkout.view.di.qualifier.CartChuckApiInterceptorQualifier;
import com.tokopedia.checkout.view.di.qualifier.CartFingerPrintApiInterceptorQualifier;
import com.tokopedia.checkout.view.di.qualifier.CartKeroRatesApiInterceptorQualifier;
import com.tokopedia.checkout.view.di.qualifier.CartKeroRatesApiRetrofitQualifier;
import com.tokopedia.checkout.view.di.qualifier.CartKeroRatesOkHttpQualifier;
import com.tokopedia.checkout.view.di.qualifier.CartQualifier;
import com.tokopedia.checkout.view.di.qualifier.CartTxActApiInterceptorQualifier;
import com.tokopedia.checkout.view.di.qualifier.CartTxActApiRetrofitQualifier;
import com.tokopedia.checkout.view.di.qualifier.CartTxActOkHttpClientQualifier;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.logisticdata.data.apiservice.RatesApi;
import com.tokopedia.logisticdata.data.constant.LogisticDataConstantUrl;

import java.util.concurrent.TimeUnit;

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
    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 0;

    @Provides
    @CartQualifier
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(
                NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY
        );
    }

    @Provides
    @CartChuckApiInterceptorQualifier
    ChuckInterceptor provideChuckInterceptor(ICheckoutModuleRouter cartCheckoutModuleRouter) {
        return cartCheckoutModuleRouter.checkoutModuleRouterGetCartCheckoutChuckInterceptor();
    }

    @Provides
    @CartFingerPrintApiInterceptorQualifier
    FingerprintInterceptor fingerprintInterceptor(ICheckoutModuleRouter cartCheckoutModuleRouter) {
        return cartCheckoutModuleRouter.checkoutModuleRouterGetCartCheckoutFingerPrintInterceptor();
    }

    @Provides
    @CartApiInterceptorQualifier
    CartApiInterceptor getCartApiInterceptor(@ApplicationContext Context context,
                                             UserSession userSession,
                                             AbstractionRouter abstractionRouter) {
        return new CartApiInterceptor(context, abstractionRouter, userSession, TkpdBaseURL.Cart.HMAC_KEY);
    }


    @Provides
    @CartKeroRatesApiInterceptorQualifier
    TkpdAuthInterceptor provideKeroRatesInterceptor(@ApplicationContext Context context,
                                                    UserSession userSession,
                                                    AbstractionRouter abstractionRouter) {
        return new TkpdAuthInterceptor(context, abstractionRouter, userSession);
    }

    @Provides
    @CartTxActApiInterceptorQualifier
    TkpdAuthInterceptor provideTxActInterceptor(@ApplicationContext Context context,
                                                UserSession userSession,
                                                AbstractionRouter abstractionRouter) {
        return new TkpdAuthInterceptor(context, abstractionRouter, userSession);
    }


    @Provides
    @CartApiOkHttpClientQualifier
    OkHttpClient provideCartApiOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            @CartApiInterceptorQualifier CartApiInterceptor cartApiInterceptor,
                                            @CartQualifier OkHttpRetryPolicy okHttpRetryPolicy,
                                            @CartFingerPrintApiInterceptorQualifier FingerprintInterceptor fingerprintInterceptor,
                                            @CartChuckApiInterceptorQualifier ChuckInterceptor chuckInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(cartApiInterceptor);
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor);
        }
        return builder.build();
    }


    @Provides
    @CartKeroRatesOkHttpQualifier
    OkHttpClient provideKeroRatesApiOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                                 @CartKeroRatesApiInterceptorQualifier TkpdAuthInterceptor keroRatesInterceptor,
                                                 @CartQualifier OkHttpRetryPolicy okHttpRetryPolicy,
                                                 @CartFingerPrintApiInterceptorQualifier FingerprintInterceptor fingerprintInterceptor,
                                                 @CartChuckApiInterceptorQualifier ChuckInterceptor chuckInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(keroRatesInterceptor);
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor);
        }
        return builder.build();
    }

    @Provides
    @CartTxActOkHttpClientQualifier
    OkHttpClient provideTxActApiOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                             @CartTxActApiInterceptorQualifier TkpdAuthInterceptor txActInterceptor,
                                             @CartQualifier OkHttpRetryPolicy okHttpRetryPolicy,
                                             @CartFingerPrintApiInterceptorQualifier FingerprintInterceptor fingerprintInterceptor,
                                             @CartChuckApiInterceptorQualifier ChuckInterceptor chuckInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(txActInterceptor);
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor);
        }
        return builder.build();
    }

    @Provides
    @CartApiRetrofitQualifier
    Retrofit provideCartApiRetrofit(
            ICheckoutModuleRouter cartCheckoutModuleRouter,
            @CartApiOkHttpClientQualifier OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(ConstantApiUrl.Cart.BASE_URL)
                .addConverterFactory(CartResponseConverter.create())
                .addConverterFactory(cartCheckoutModuleRouter.checkoutModuleRouterGetStringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @CartKeroRatesApiRetrofitQualifier
    Retrofit provideCartKeroRatesRetrofit(
            ICheckoutModuleRouter cartCheckoutModuleRouter,
            @CartKeroRatesOkHttpQualifier OkHttpClient okHttpClient
    ) {
        return new Retrofit.Builder()
                .baseUrl(LogisticDataConstantUrl.KeroRates.BASE_URL)
                .addConverterFactory(cartCheckoutModuleRouter.checkoutModuleRouterGetWS4TkpdResponseConverter())
                .addConverterFactory(cartCheckoutModuleRouter.checkoutModuleRouterGetStringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }


    @Provides
    @CartTxActApiRetrofitQualifier
    Retrofit provideCartTxActRetrofit(
            ICheckoutModuleRouter cartCheckoutModuleRouter,
            @CartTxActOkHttpClientQualifier OkHttpClient okHttpClient
    ) {
        return new Retrofit.Builder()
                .baseUrl(ConstantApiUrl.TransactionAction.BASE_URL)
                .addConverterFactory(cartCheckoutModuleRouter.checkoutModuleRouterGetWS4TkpdResponseConverter())
                .addConverterFactory(cartCheckoutModuleRouter.checkoutModuleRouterGetStringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @CartQualifier
    CartApi provideCartApi(@CartApiRetrofitQualifier Retrofit retrofit) {
        return retrofit.create(CartApi.class);
    }

    @Provides
    @CartQualifier
    RatesApi provideRatesApi(@CartKeroRatesApiRetrofitQualifier Retrofit retrofit) {
        return retrofit.create(RatesApi.class);
    }

    @Provides
    @CartQualifier
    TxActApi provideTxActsApi(@CartTxActApiRetrofitQualifier Retrofit retrofit) {
        return retrofit.create(TxActApi.class);
    }

    @Provides
    ICartRepository provideICartRepository(@CartQualifier CartApi cartApi) {
        return new CartRepository(cartApi);
    }

    @Provides
    ITopPayRepository provideITopPayRepository(@CartQualifier TxActApi txActApi) {
        return new TopPayRepository(txActApi);
    }

    @Provides
    RatesRepository provideRatesRepository(@CartQualifier RatesApi ratesApi) {
        return new RatesRepository(ratesApi);
    }

}
