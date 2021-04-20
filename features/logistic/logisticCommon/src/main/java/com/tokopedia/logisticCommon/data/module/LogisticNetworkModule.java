package com.tokopedia.logisticCommon.data.module;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerCollector;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.logisticCommon.data.apiservice.InsuranceApi;
import com.tokopedia.logisticCommon.data.apiservice.MyShopOrderActApi;
import com.tokopedia.logisticCommon.data.apiservice.MyShopOrderApi;
import com.tokopedia.logisticCommon.data.apiservice.OrderDetailApi;
import com.tokopedia.logisticCommon.data.apiservice.PeopleActApi;
import com.tokopedia.logisticCommon.data.converter.GeneratedHostConverter;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticAbstractionRouterQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticChuckInterceptorQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticContextQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticDebugInterceptorQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticFingerprintInterceptorQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticGsonConverterFactoryQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticInsuranceApiQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticMyShopOrderActApiQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticMyShopOrderApiQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticNetworkRouterQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticOkHttpRetryPolicyNoAutoRetryQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticOrderDetailApiQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticPeopleActApiQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticRxJavaCallAdapterFactoryQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticStringResponseConverterQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticTokopediaAuthInterceptorQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticTokopediaWsV4ResponseConverterQualifier;
import com.tokopedia.logisticCommon.data.module.qualifier.LogisticUserSessionQualifier;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class LogisticNetworkModule {

    @Provides
    @LogisticContextQualifier
    Context provideLogisticContext(@ApplicationContext Context context) {
        return context;
    }

    @Provides
    @LogisticNetworkRouterQualifier
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return ((NetworkRouter) context);
    }

    @Provides
    @LogisticUserSessionQualifier
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    @LogisticAbstractionRouterQualifier
    AbstractionRouter provideAbstractionRouter(@ApplicationContext Context context) {
        return ((AbstractionRouter) context);
    }

    @Provides
    @LogisticTokopediaWsV4ResponseConverterQualifier
    Converter.Factory provideTokopediaWsV4ResponseConverter() {
        return new TokopediaWsV4ResponseConverter();
    }


    @Provides
    @LogisticStringResponseConverterQualifier
    Converter.Factory provideStringResponseConverter() {
        return new StringResponseConverter();
    }

    @Provides
    @LogisticGsonConverterFactoryQualifier
    Converter.Factory provideGsonConverter() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @LogisticGsonConverterFactoryQualifier
    CallAdapter.Factory provideRxJavaCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }

    @Provides
    @LogisticFingerprintInterceptorQualifier
    Interceptor provideFingerprintInterceptor(
            @LogisticNetworkRouterQualifier NetworkRouter networkRouter,
            @LogisticUserSessionQualifier UserSessionInterface userSessionInterface
    ) {
        return new FingerprintInterceptor(networkRouter, userSessionInterface);
    }

    @Provides
    @LogisticRxJavaCallAdapterFactoryQualifier
    CallAdapter.Factory RxJavaCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }

    @Provides
    @LogisticTokopediaAuthInterceptorQualifier
    Interceptor provideTokopediaAuthInterceptor(
            @LogisticContextQualifier Context context,
            @LogisticNetworkRouterQualifier NetworkRouter networkRouter,
            @LogisticUserSessionQualifier UserSessionInterface userSessionInterface
    ) {
        return new TkpdAuthInterceptor(context, networkRouter, userSessionInterface);
    }

    @Provides
    @LogisticChuckInterceptorQualifier
    Interceptor provideChuckerInterceptor(@LogisticContextQualifier Context context,
                                        @LogisticAbstractionRouterQualifier AbstractionRouter abstractionRouter) {

        ChuckerCollector collector = new ChuckerCollector(
                context, abstractionRouter.isAllowLogOnChuckInterceptorNotification());

        return new ChuckerInterceptor(context, collector);
    }

    @Provides
    @LogisticDebugInterceptorQualifier
    Interceptor provideDebugInterceptor() {
        return new DebugInterceptor();
    }

    @Provides
    @LogisticOkHttpRetryPolicyNoAutoRetryQualifier
    OkHttpRetryPolicy provideOkHttpRetryPolicyNoAutoRetry() {
        return OkHttpRetryPolicy.createdOkHttpNoAutoRetryPolicy();
    }

    @Provides
    @LogisticInsuranceApiQualifier
    InsuranceApi provideInsuranceApi(
            @LogisticOkHttpRetryPolicyNoAutoRetryQualifier OkHttpRetryPolicy okHttpRetryPolicy,
            @LogisticTokopediaWsV4ResponseConverterQualifier Converter.Factory tokopediaWsV4ResponseConverter,
            @LogisticStringResponseConverterQualifier Converter.Factory stringResponseConverter,
            @LogisticGsonConverterFactoryQualifier Converter.Factory gsonConverterFactory,
            @LogisticRxJavaCallAdapterFactoryQualifier CallAdapter.Factory rxJavaCallAdapterFactory,
            @LogisticFingerprintInterceptorQualifier Interceptor fingerPrintInterceptor,
            @LogisticTokopediaAuthInterceptorQualifier Interceptor tokopediaAuthInterceptor,
            @LogisticChuckInterceptorQualifier Interceptor chuckInterceptor,
            @LogisticDebugInterceptorQualifier Interceptor debugInterceptor

    ) {
        Retrofit.Builder insuranceApiBuilder = new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.Shop.URL_SHIPPING_WEBVIEW)
                .addConverterFactory(tokopediaWsV4ResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory);

        OkHttpClient.Builder okHttpClientInsuranceApiBuilder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerPrintInterceptor)
                .addInterceptor(tokopediaAuthInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            okHttpClientInsuranceApiBuilder.addInterceptor(chuckInterceptor);
            okHttpClientInsuranceApiBuilder.addInterceptor(debugInterceptor);
        }
        return insuranceApiBuilder.client(okHttpClientInsuranceApiBuilder.build())
                .build().create(InsuranceApi.class);
    }


    @Provides
    @LogisticPeopleActApiQualifier
    PeopleActApi providePeopleActApi(
            @LogisticOkHttpRetryPolicyNoAutoRetryQualifier OkHttpRetryPolicy okHttpRetryPolicy,
            @LogisticTokopediaWsV4ResponseConverterQualifier Converter.Factory tokopediaWsV4ResponseConverter,
            @LogisticStringResponseConverterQualifier Converter.Factory stringResponseConverter,
            @LogisticGsonConverterFactoryQualifier Converter.Factory gsonConverterFactory,
            @LogisticRxJavaCallAdapterFactoryQualifier CallAdapter.Factory rxJavaCallAdapterFactory,
            @LogisticFingerprintInterceptorQualifier Interceptor fingerPrintInterceptor,
            @LogisticTokopediaAuthInterceptorQualifier Interceptor tokopediaAuthInterceptor,
            @LogisticChuckInterceptorQualifier Interceptor chuckInterceptor,
            @LogisticDebugInterceptorQualifier Interceptor debugInterceptor

    ) {

        OkHttpClient.Builder okHttpPeopleActApiBuilder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerPrintInterceptor)
                .addInterceptor(tokopediaAuthInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            okHttpPeopleActApiBuilder.addInterceptor(chuckInterceptor);
            okHttpPeopleActApiBuilder.addInterceptor(debugInterceptor);
        }
        return new Retrofit.Builder()
                .baseUrl(TokopediaUrl.Companion.getInstance().getWS())
                .addConverterFactory(tokopediaWsV4ResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .client(okHttpPeopleActApiBuilder.build()).build().create(PeopleActApi.class);
    }

    @Provides
    @LogisticMyShopOrderApiQualifier
    MyShopOrderApi provideMyShopOrderApi(
            @LogisticOkHttpRetryPolicyNoAutoRetryQualifier OkHttpRetryPolicy okHttpRetryPolicy,
            @LogisticTokopediaWsV4ResponseConverterQualifier Converter.Factory tokopediaWsV4ResponseConverter,
            @LogisticStringResponseConverterQualifier Converter.Factory stringResponseConverter,
            @LogisticGsonConverterFactoryQualifier Converter.Factory gsonConverterFactory,
            @LogisticRxJavaCallAdapterFactoryQualifier CallAdapter.Factory rxJavaCallAdapterFactory,
            @LogisticFingerprintInterceptorQualifier Interceptor fingerPrintInterceptor,
            @LogisticTokopediaAuthInterceptorQualifier Interceptor tokopediaAuthInterceptor,
            @LogisticChuckInterceptorQualifier Interceptor chuckInterceptor,
            @LogisticDebugInterceptorQualifier Interceptor debugInterceptor

    ) {
        Retrofit.Builder retrofitMyShopOrderApiBuilder = new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.Shop.URL_MY_SHOP_ORDER)
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(tokopediaWsV4ResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory);
        OkHttpClient.Builder okHttpClientMyShopOrderApiBuilder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerPrintInterceptor)
                .addInterceptor(tokopediaAuthInterceptor);
        if (GlobalConfig.isAllowDebuggingTools()) {
            okHttpClientMyShopOrderApiBuilder.addInterceptor(chuckInterceptor);
            okHttpClientMyShopOrderApiBuilder.addInterceptor(debugInterceptor);
        }

        return retrofitMyShopOrderApiBuilder.client(okHttpClientMyShopOrderApiBuilder.build())
                .build().create(MyShopOrderApi.class);
    }


    @Provides
    @LogisticMyShopOrderActApiQualifier
    MyShopOrderActApi provideMyShopOrderActApi(
            @LogisticOkHttpRetryPolicyNoAutoRetryQualifier OkHttpRetryPolicy okHttpRetryPolicy,
            @LogisticTokopediaWsV4ResponseConverterQualifier Converter.Factory tokopediaWsV4ResponseConverter,
            @LogisticStringResponseConverterQualifier Converter.Factory stringResponseConverter,
            @LogisticGsonConverterFactoryQualifier Converter.Factory gsonConverterFactory,
            @LogisticRxJavaCallAdapterFactoryQualifier CallAdapter.Factory rxJavaCallAdapterFactory,
            @LogisticFingerprintInterceptorQualifier Interceptor fingerPrintInterceptor,
            @LogisticTokopediaAuthInterceptorQualifier Interceptor tokopediaAuthInterceptor,
            @LogisticChuckInterceptorQualifier Interceptor chuckInterceptor,
            @LogisticDebugInterceptorQualifier Interceptor debugInterceptor

    ) {
        Retrofit.Builder retrofitMyShopOrderActApiBuilder = new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.Shop.URL_MY_SHOP_ORDER_ACTION)
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(tokopediaWsV4ResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory);
        OkHttpClient.Builder okHttpClientMyShopOrderActApiBuilder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerPrintInterceptor)
                .addInterceptor(tokopediaAuthInterceptor);
        if (GlobalConfig.isAllowDebuggingTools()) {
            okHttpClientMyShopOrderActApiBuilder.addInterceptor(chuckInterceptor);
            okHttpClientMyShopOrderActApiBuilder.addInterceptor(debugInterceptor);
        }

        return retrofitMyShopOrderActApiBuilder.client(okHttpClientMyShopOrderActApiBuilder.build())
                .build().create(MyShopOrderActApi.class);
    }

    @Provides
    @LogisticOrderDetailApiQualifier
    OrderDetailApi provideOrderDetailApi(
            @LogisticOkHttpRetryPolicyNoAutoRetryQualifier OkHttpRetryPolicy okHttpRetryPolicy,
            @LogisticTokopediaWsV4ResponseConverterQualifier Converter.Factory tokopediaWsV4ResponseConverter,
            @LogisticStringResponseConverterQualifier Converter.Factory stringResponseConverter,
            @LogisticGsonConverterFactoryQualifier Converter.Factory gsonConverterFactory,
            @LogisticRxJavaCallAdapterFactoryQualifier CallAdapter.Factory rxJavaCallAdapterFactory,
            @LogisticFingerprintInterceptorQualifier Interceptor fingerPrintInterceptor,
            @LogisticTokopediaAuthInterceptorQualifier Interceptor tokopediaAuthInterceptor,
            @LogisticChuckInterceptorQualifier Interceptor chuckInterceptor,
            @LogisticDebugInterceptorQualifier Interceptor debugInterceptor

    ) {
        Retrofit.Builder retrofitOrderDetailApiBuilder = new Retrofit.Builder()
                .baseUrl(TokopediaUrl.Companion.getInstance().getWS())
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(tokopediaWsV4ResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory);
        OkHttpClient.Builder okHttpClientOrderDetailtApiBuilder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerPrintInterceptor)
                .addInterceptor(tokopediaAuthInterceptor);
        if (GlobalConfig.isAllowDebuggingTools()) {
            okHttpClientOrderDetailtApiBuilder.addInterceptor(chuckInterceptor);
            okHttpClientOrderDetailtApiBuilder.addInterceptor(debugInterceptor);
        }

        return retrofitOrderDetailApiBuilder.client(okHttpClientOrderDetailtApiBuilder.build())
                .build().create(OrderDetailApi.class);
    }

}
