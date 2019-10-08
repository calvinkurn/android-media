package com.tokopedia.checkout.view.di.module;

import android.content.Context;

import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor;
import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.checkout.data.mapper.AddressModelMapper;
import com.tokopedia.checkout.data.repository.AddressRepository;
import com.tokopedia.checkout.data.repository.AddressRepositoryImpl;
import com.tokopedia.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.checkout.data.repository.PeopleAddressRepositoryImpl;
import com.tokopedia.checkout.domain.usecase.GetAddressWithCornerUseCase;
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
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.logisticdata.data.apiservice.RatesApi;
import com.tokopedia.logisticdata.data.constant.LogisticDataConstantUrl;
import com.tokopedia.logisticdata.data.repository.RatesRepository;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.transactiondata.apiservice.CartApi;
import com.tokopedia.transactiondata.apiservice.CartApiInterceptor;
import com.tokopedia.transactiondata.apiservice.CartResponseConverter;
import com.tokopedia.transactiondata.apiservice.TxActApi;
import com.tokopedia.transactiondata.constant.TransactionDataApiUrl;
import com.tokopedia.transactiondata.repository.CartRepository;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.transactiondata.repository.ITopPayRepository;
import com.tokopedia.transactiondata.repository.TopPayRepository;
import com.tokopedia.user.session.UserSessionInterface;

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
    NetworkRouter provideNetworkRouter (@ApplicationContext Context context) {
        return (NetworkRouter)context;
    }

    @Provides
    @CartChuckApiInterceptorQualifier
    Interceptor provideChuckInterceptor(ICheckoutModuleRouter cartCheckoutModuleRouter) {
        return cartCheckoutModuleRouter.checkoutModuleRouterGetCartCheckoutChuckInterceptor();
    }

    @Provides
    @CartFingerPrintApiInterceptorQualifier
    Interceptor fingerprintInterceptor(ICheckoutModuleRouter cartCheckoutModuleRouter) {
        return cartCheckoutModuleRouter.checkoutModuleRouterGetCartCheckoutFingerPrintInterceptor();
    }

    @Provides
    @CartApiInterceptorQualifier
    CartApiInterceptor getCartApiInterceptor(@ApplicationContext Context context,
                                             NetworkRouter networkRouter,
                                             UserSessionInterface userSession) {
        return new CartApiInterceptor(context, networkRouter, userSession, TransactionDataApiUrl.Cart.HMAC_KEY);
    }


    @Provides
    @CartKeroRatesApiInterceptorQualifier
    TkpdAuthInterceptor provideKeroRatesInterceptor(@ApplicationContext Context context,
                                                    NetworkRouter networkRouter,
                                                    UserSessionInterface userSession) {
        return new TkpdAuthInterceptor(context, networkRouter, userSession);
    }

    @Provides
    @CartTxActApiInterceptorQualifier
    TkpdAuthInterceptor provideTxActInterceptor(@ApplicationContext Context context,
                                                NetworkRouter networkRouter,
                                                UserSessionInterface userSession) {
        return new TkpdAuthInterceptor(context, networkRouter, userSession);
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
    @CartKeroRatesOkHttpQualifier
    OkHttpClient provideKeroRatesApiOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                                 @CartKeroRatesApiInterceptorQualifier TkpdAuthInterceptor keroRatesInterceptor,
                                                 @CartQualifier OkHttpRetryPolicy okHttpRetryPolicy,
                                                 @CartFingerPrintApiInterceptorQualifier Interceptor fingerprintInterceptor,
                                                 @CartChuckApiInterceptorQualifier Interceptor chuckInterceptor) {

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
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
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
                .baseUrl(TransactionDataApiUrl.TransactionAction.BASE_URL)
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
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
    @CartQualifier
    PeopleActApi providePeopleActApi(@CartTxActApiRetrofitQualifier Retrofit retrofit) {
        return retrofit.create(PeopleActApi.class);
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

    @Provides
    AddressRepository provideAddressRepository(@CartQualifier PeopleActApi peopleActApi) {
        return new AddressRepositoryImpl(peopleActApi);
    }

    @Provides
    AddressModelMapper providePeopleAddressMapper() {
        return new AddressModelMapper();
    }

    @Provides
    PeopleAddressRepository providePeopleAddressRepository(@CartQualifier PeopleActApi peopleActApi,
                                                           GetAddressWithCornerUseCase addressWithCornerUseCase) {
        return new PeopleAddressRepositoryImpl(peopleActApi, addressWithCornerUseCase);
    }

    @Provides
    GetAddressWithCornerUseCase provideGetAddressWithCornerUsecase(@ApplicationScope Context context) {
        return new GetAddressWithCornerUseCase(context, new GraphqlUseCase());
    }

}
