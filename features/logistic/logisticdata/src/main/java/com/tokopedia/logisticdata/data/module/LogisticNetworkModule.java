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
import com.tokopedia.logisticdata.data.module.qualifier.InsuranceTnCScope;
import com.tokopedia.logisticdata.data.repository.InsuranceTnCDataStore;
import com.tokopedia.logisticdata.data.repository.InsuranceTnCRepository;
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
    private static final int NET_READ_TIMEOUT = 100;
    private static final int NET_WRITE_TIMEOUT = 100;
    private static final int NET_CONNECT_TIMEOUT = 100;
    private static final int NET_RETRY = 0;

    @Provides
    @InsuranceTnCScope
    InsuranceApi provideInsuranceApi(@ApplicationContext Context context) {

        NetworkRouter networkRouter = ((NetworkRouter) context);
        UserSessionInterface userSession = new UserSession(context);
        AbstractionRouter abstractionRouter = ((AbstractionRouter) context);

        OkHttpRetryPolicy okHttpRetryPolicy = new OkHttpRetryPolicy(
                NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY
        );

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        Converter.Factory tokopediaWsV4ResponseConverter = new TokopediaWsV4ResponseConverter();
        Converter.Factory stringResponseConverter = new StringResponseConverter();
        Converter.Factory gsonConverterFactory = GsonConverterFactory.create(gson);
        CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

        Interceptor fingerPrintInterceptor = new FingerprintInterceptor(networkRouter, userSession);
        Interceptor tokopediaAuthInterceptor = new TkpdAuthInterceptor(context, networkRouter, userSession);
        Interceptor cacheApiInterceptor = new CacheApiInterceptor();

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
            Interceptor chuckInterceptor = new ChuckInterceptor(context)
                    .showNotification(abstractionRouter.isAllowLogOnChuckInterceptorNotification());
            Interceptor debugInterceptor = new DebugInterceptor();

            okHttpClientInsuranceApiBuilder.addInterceptor(chuckInterceptor);
            okHttpClientInsuranceApiBuilder.addInterceptor(debugInterceptor);
        }


        return insuranceApiBuilder.client(okHttpClientInsuranceApiBuilder.build())
                .build().create(InsuranceApi.class);
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
