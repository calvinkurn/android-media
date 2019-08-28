package com.tokopedia.purchase_platform.common.di.module;

import android.content.Context;

import com.example.akamai_bot_lib.interceptor.AkamaiBotInterceptor;
import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.purchase_platform.features.checkout.data.AddressRepository;
import com.tokopedia.purchase_platform.features.checkout.data.AddressRepositoryImpl;
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.mapper.AddressModelMapper;
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.usecase.GetAddressWithCornerUseCase;
import com.tokopedia.purchase_platform.common.data.apiservice.CartApi;
import com.tokopedia.purchase_platform.common.data.common.api.CartApiInterceptor;
import com.tokopedia.purchase_platform.common.data.common.api.CartResponseConverter;
import com.tokopedia.purchase_platform.common.data.apiservice.TransactionDataApiUrl;
import com.tokopedia.purchase_platform.common.data.repository.CartRepository;
import com.tokopedia.purchase_platform.common.data.repository.ICartRepository;
import com.tokopedia.purchase_platform.common.di.qualifier.CartApiInterceptorQualifier;
import com.tokopedia.purchase_platform.common.di.qualifier.CartApiOkHttpClientQualifier;
import com.tokopedia.purchase_platform.common.di.qualifier.CartApiRetrofitQualifier;
import com.tokopedia.purchase_platform.common.di.qualifier.CartChuckApiInterceptorQualifier;
import com.tokopedia.purchase_platform.common.di.qualifier.CartFingerPrintApiInterceptorQualifier;
import com.tokopedia.purchase_platform.common.di.qualifier.CartQualifier;
import com.tokopedia.purchase_platform.common.di.qualifier.CartTxActApiInterceptorQualifier;
import com.tokopedia.purchase_platform.common.di.qualifier.CartTxActApiRetrofitQualifier;
import com.tokopedia.purchase_platform.common.di.qualifier.CartTxActOkHttpClientQualifier;
import com.tokopedia.purchase_platform.common.router.ICheckoutModuleRouter;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
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

    public DataModule() {
    }

    @Provides
    @CartQualifier
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(
                NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY
        );
    }

    @Provides
    @CartChuckApiInterceptorQualifier
    Interceptor provideChuckInterceptor(ICheckoutModuleRouter cartCheckoutModuleRouter) {
        return cartCheckoutModuleRouter.getChuckInterceptor();
    }

    @Provides
    @CartFingerPrintApiInterceptorQualifier
    Interceptor fingerprintInterceptor(ICheckoutModuleRouter cartCheckoutModuleRouter) {
        return cartCheckoutModuleRouter.getFingerPrintInterceptor();
    }

    @Provides
    @CartApiInterceptorQualifier
    CartApiInterceptor getCartApiInterceptor(@ApplicationContext Context context,
                                             AbstractionRouter abstractionRouter) {
        return null;
//        return new CartApiInterceptor(context, abstractionRouter, TransactionDataApiUrl.Cart.HMAC_KEY);
    }


    @Provides
    @CartTxActApiInterceptorQualifier
    TkpdAuthInterceptor provideTxActInterceptor(@ApplicationContext Context context,
                                                AbstractionRouter abstractionRouter) {
        return new TkpdAuthInterceptor(context, abstractionRouter);
    }


    @Provides
    @CartApiOkHttpClientQualifier
    OkHttpClient provideCartApiOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            @CartApiInterceptorQualifier CartApiInterceptor cartApiInterceptor,
                                            @CartQualifier OkHttpRetryPolicy okHttpRetryPolicy,
                                            @CartFingerPrintApiInterceptorQualifier Interceptor fingerprintInterceptor,
                                            @CartChuckApiInterceptorQualifier Interceptor chuckInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(new AkamaiBotInterceptor())
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(cartApiInterceptor);
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
                                             @CartFingerPrintApiInterceptorQualifier Interceptor fingerprintInterceptor,
                                             @CartChuckApiInterceptorQualifier Interceptor chuckInterceptor) {

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
                .baseUrl(TransactionDataApiUrl.Cart.BASE_URL)
                .addConverterFactory(CartResponseConverter.create())
                .addConverterFactory(cartCheckoutModuleRouter.getStringResponseConverter())
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
                .baseUrl(TransactionDataApiUrl.TransactionAction.BASE_URL)
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(cartCheckoutModuleRouter.getStringResponseConverter())
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
    PeopleActApi providePeopleActApi(@CartTxActApiRetrofitQualifier Retrofit retrofit) {
        return retrofit.create(PeopleActApi.class);
    }

    @Provides
    ICartRepository provideICartRepository(@CartQualifier CartApi cartApi) {
        return new CartRepository(cartApi);
    }

    @Provides
    AddressRepository provideAddressRepository(@CartQualifier PeopleActApi peopleActApi) {
        return new AddressRepositoryImpl(peopleActApi);
    }

    @Provides
    AddressModelMapper providePeopleAddressMapper() {
        return new AddressModelMapper();
    }

    @Provides
    GetAddressWithCornerUseCase provideGetAddressWithCornerUsecase(@ApplicationScope Context context) {
        return new GetAddressWithCornerUseCase(context, new GraphqlUseCase());
    }

}
