package com.tokopedia.logisticdata.data.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.logisticdata.data.apiservice.InsuranceApi;
import com.tokopedia.logisticdata.data.apiservice.MyShopOrderActApi;
import com.tokopedia.logisticdata.data.apiservice.MyShopOrderApi;
import com.tokopedia.logisticdata.data.apiservice.OrderDetailApi;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.logisticdata.data.converter.GeneratedHostConverter;
import com.tokopedia.logisticdata.data.module.qualifier.AddressScope;
import com.tokopedia.logisticdata.data.module.qualifier.CourierDataRepositoryQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.InsuranceTnCScope;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticAbstractionRouterQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticCacheApiInterceptorQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticChuckInterceptorQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticContextQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticDebugInterceptorQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticFingerprintInterceptorQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticGsonConverterFactoryQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticNetworkRouterQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticOkHttpRetryPolicyNoAutoRetryQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticRxJavaCallAdapterFactoryQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticStringResponseConverterQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticTokopediaAuthInterceptorQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticTokopediaWsV4ResponseConverterQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticUserSessionQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.OrderCourierScope;
import com.tokopedia.logisticdata.data.repository.InsuranceTnCDataStore;
import com.tokopedia.logisticdata.data.repository.InsuranceTnCRepository;
import com.tokopedia.logisticdata.data.repository.OrderCourierRepository;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;
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
    @LogisticTokopediaAuthInterceptorQualifier
    Interceptor provideTokopediaAuthInterceptor(
            @LogisticContextQualifier Context context,
            @LogisticNetworkRouterQualifier NetworkRouter networkRouter,
            @LogisticUserSessionQualifier UserSessionInterface userSessionInterface
    ) {
        return new TkpdAuthInterceptor(context, networkRouter, userSessionInterface);
    }

    @Provides
    @LogisticCacheApiInterceptorQualifier
    Interceptor provideCacheApiInterceptor() {
        return new CacheApiInterceptor();
    }

    @Provides
    @LogisticChuckInterceptorQualifier
    Interceptor provideChuckInterceptor(@LogisticContextQualifier Context context,
                                        @LogisticAbstractionRouterQualifier AbstractionRouter abstractionRouter) {
        ChuckInterceptor chuckInterceptor = new ChuckInterceptor(context);

        chuckInterceptor.showNotification(abstractionRouter.isAllowLogOnChuckInterceptorNotification());
        return chuckInterceptor;
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
    @InsuranceTnCScope
    InsuranceApi provideInsuranceApi(
            @LogisticOkHttpRetryPolicyNoAutoRetryQualifier OkHttpRetryPolicy okHttpRetryPolicy,
            @LogisticTokopediaWsV4ResponseConverterQualifier TokopediaWsV4ResponseConverter tokopediaWsV4ResponseConverter,
            @LogisticStringResponseConverterQualifier StringResponseConverter stringResponseConverter,
            @LogisticGsonConverterFactoryQualifier GsonConverterFactory gsonConverterFactory,
            @LogisticRxJavaCallAdapterFactoryQualifier CallAdapter.Factory rxJavaCallAdapterFactory,
            @LogisticFingerprintInterceptorQualifier Interceptor fingerPrintInterceptor,
            @LogisticTokopediaAuthInterceptorQualifier Interceptor tokopediaAuthInterceptor,
            @LogisticCacheApiInterceptorQualifier Interceptor cacheApiInterceptor,
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
                .addInterceptor(tokopediaAuthInterceptor)
                .addInterceptor(cacheApiInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            okHttpClientInsuranceApiBuilder.addInterceptor(chuckInterceptor);
            okHttpClientInsuranceApiBuilder.addInterceptor(debugInterceptor);
        }
        return insuranceApiBuilder.client(okHttpClientInsuranceApiBuilder.build())
                .build().create(InsuranceApi.class);
    }


    @Provides
    @AddressScope
    PeopleActApi providePeopleActApi(
            @LogisticOkHttpRetryPolicyNoAutoRetryQualifier OkHttpRetryPolicy okHttpRetryPolicy,
            @LogisticTokopediaWsV4ResponseConverterQualifier TokopediaWsV4ResponseConverter tokopediaWsV4ResponseConverter,
            @LogisticStringResponseConverterQualifier StringResponseConverter stringResponseConverter,
            @LogisticGsonConverterFactoryQualifier GsonConverterFactory gsonConverterFactory,
            @LogisticRxJavaCallAdapterFactoryQualifier CallAdapter.Factory rxJavaCallAdapterFactory,
            @LogisticFingerprintInterceptorQualifier Interceptor fingerPrintInterceptor,
            @LogisticTokopediaAuthInterceptorQualifier Interceptor tokopediaAuthInterceptor,
            @LogisticCacheApiInterceptorQualifier Interceptor cacheApiInterceptor,
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
                .baseUrl(TkpdBaseURL.BASE_DOMAIN)
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpPeopleActApiBuilder.build()).build().create(PeopleActApi.class);
    }


    @Provides
    @OrderCourierScope
    @CourierDataRepositoryQualifier
    OrderCourierRepository provideOrderCourierRepository(@ApplicationContext Context context) {

        NetworkRouter networkRouter = ((NetworkRouter) context);
        UserSessionInterface userSession = new UserSession(context);
        AbstractionRouter abstractionRouter = ((AbstractionRouter) context);

        OkHttpRetryPolicy okHttpRetryPolicy = OkHttpRetryPolicy.createdOkHttpNoAutoRetryPolicy();


        MyShopOrderApi myShopOrderApi;
        MyShopOrderActApi myShopOrderActApi;
        OrderDetailApi orderDetailApi = null;

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        Converter.Factory generatedHostConverterFactory = new GeneratedHostConverter();
        Converter.Factory tokopediaWsV4ResponseConverter = new TokopediaWsV4ResponseConverter();
        Converter.Factory stringResponseConverter = new StringResponseConverter();
        Converter.Factory gsonConverterFactory = GsonConverterFactory.create(gson);
        CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

        Interceptor fingerPrintInterceptor = new FingerprintInterceptor(networkRouter, userSession);
        Interceptor tokopediaAuthInterceptor = new TkpdAuthInterceptor(context, networkRouter, userSession);
        Interceptor cacheApiInterceptor = new CacheApiInterceptor();

        Retrofit.Builder retrofitMyShopOrderApiBuilder = new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.Shop.URL_MY_SHOP_ORDER)
                .addConverterFactory(generatedHostConverterFactory)
                .addConverterFactory(tokopediaWsV4ResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory);
        OkHttpClient.Builder okHttpClientMyShopOrderApiBuilder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerPrintInterceptor)
                .addInterceptor(tokopediaAuthInterceptor)
                .addInterceptor(cacheApiInterceptor);

        Retrofit.Builder retrofitMyShopOrderActApiBuilder = new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.Shop.URL_MY_SHOP_ORDER_ACTION)
                .addConverterFactory(generatedHostConverterFactory)
                .addConverterFactory(tokopediaWsV4ResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory);
        OkHttpClient.Builder okHttpClientMyShopOrderActApiBuilder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerPrintInterceptor)
                .addInterceptor(tokopediaAuthInterceptor)
                .addInterceptor(cacheApiInterceptor);


        Retrofit.Builder retrofitOrderDetailApiBuilder = new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.BASE_DOMAIN)
                .addConverterFactory(generatedHostConverterFactory)
                .addConverterFactory(tokopediaWsV4ResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory);
        OkHttpClient.Builder okHttpClientOrderDetailtApiBuilder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerPrintInterceptor)
                .addInterceptor(tokopediaAuthInterceptor)
                .addInterceptor(cacheApiInterceptor);


        if (GlobalConfig.isAllowDebuggingTools()) {

            Interceptor chuckInterceptor = new ChuckInterceptor(context)
                    .showNotification(abstractionRouter.isAllowLogOnChuckInterceptorNotification());
            Interceptor debugInterceptor = new DebugInterceptor();

            okHttpClientMyShopOrderApiBuilder.addInterceptor(chuckInterceptor);
            okHttpClientMyShopOrderApiBuilder.addInterceptor(debugInterceptor);

            okHttpClientMyShopOrderActApiBuilder.addInterceptor(chuckInterceptor);
            okHttpClientMyShopOrderActApiBuilder.addInterceptor(debugInterceptor);

            okHttpClientOrderDetailtApiBuilder.addInterceptor(chuckInterceptor);
            okHttpClientOrderDetailtApiBuilder.addInterceptor(debugInterceptor);
        }


        myShopOrderApi = retrofitMyShopOrderApiBuilder.client(okHttpClientMyShopOrderApiBuilder.build())
                .build().create(MyShopOrderApi.class);

        myShopOrderActApi = retrofitMyShopOrderActApiBuilder.client(okHttpClientMyShopOrderActApiBuilder.build())
                .build().create(MyShopOrderActApi.class);

        orderDetailApi = retrofitOrderDetailApiBuilder.client(okHttpClientOrderDetailtApiBuilder.build())
                .build().create(OrderDetailApi.class);


        return new OrderCourierRepository(
                myShopOrderApi,
                myShopOrderActApi,
                orderDetailApi
        );
    }


    // Provide Data Store
    @Provides
    @InsuranceTnCScope
    InsuranceTnCDataStore provideInsuranceTnCDataStore(InsuranceApi insuranceApi) {
        return new InsuranceTnCDataStore(insuranceApi);
    }

    // Provide Repository
    @Provides
    @InsuranceTnCScope
    InsuranceTnCRepository provideInsuranceTnCRepository(InsuranceTnCDataStore insuranceTnCDataStore) {
        return new InsuranceTnCRepository(insuranceTnCDataStore);
    }

}
