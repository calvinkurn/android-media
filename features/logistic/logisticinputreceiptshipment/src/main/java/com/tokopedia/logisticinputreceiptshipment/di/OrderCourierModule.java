package com.tokopedia.logisticinputreceiptshipment.di;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.logisticanalytics.SalesShippingAnalytics;
import com.tokopedia.logisticinputreceiptshipment.view.confirmshipment.OrderCourierInteractorImpl;
import com.tokopedia.logisticinputreceiptshipment.view.confirmshipment.OrderCourierPresenterImpl;
import com.tokopedia.logisticdata.data.repository.OrderCourierRepository;
import com.tokopedia.logisticdata.data.converter.GeneratedHostConverter;
import com.tokopedia.logisticdata.data.apiservice.MyShopOrderActApi;
import com.tokopedia.logisticdata.data.apiservice.MyShopOrderApi;
import com.tokopedia.logisticdata.data.apiservice.OrderDetailApi;
import com.tokopedia.logisticinputreceiptshipment.network.mapper.OrderDetailMapper;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

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
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 1/3/18. Tokopedia
 */
@Module
public class OrderCourierModule {

    private static final int NET_READ_TIMEOUT = 100;
    private static final int NET_WRITE_TIMEOUT = 100;
    private static final int NET_CONNECT_TIMEOUT = 100;
    private static final int NET_RETRY = 0;

    public OrderCourierModule() {
    }

    @Provides
    @OrderCourierScope
    CompositeSubscription provideCompositeSubsrciption() {
        return new CompositeSubscription();
    }

    @Provides
    @OrderCourierScope
    OrderDetailMapper provideOrderDetailMapper() {
        return new OrderDetailMapper();
    }

    @Provides
    @OrderCourierScope
    @CourierDataRepositoryQualifier
    OrderCourierRepository provideOrderCourierRepository(@ApplicationContext Context context) {

        NetworkRouter networkRouter = ((NetworkRouter) context);
        UserSession userSession = new UserSession(context);
        AbstractionRouter abstractionRouter = ((AbstractionRouter) context);

        OkHttpRetryPolicy okHttpRetryPolicy = new OkHttpRetryPolicy(
                NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY
        );


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

    @Provides
    @OrderCourierScope
    OrderCourierInteractorImpl provideOrderCourierInteractor(
            @CourierDataRepositoryQualifier OrderCourierRepository courierRepository) {
        return new OrderCourierInteractorImpl(provideCompositeSubsrciption(),
                courierRepository, provideOrderDetailMapper());
    }

    @Provides
    @OrderCourierScope
    OrderCourierPresenterImpl provideOrderCourierPresenter(@ApplicationContext Context context,
                                                           OrderCourierInteractorImpl orderCourierInteractor) {
        return new OrderCourierPresenterImpl(orderCourierInteractor, new UserSession(context));
    }

    @Provides
    @OrderCourierScope
    SalesShippingAnalytics provideSalesShippingAnalytics(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return new SalesShippingAnalytics(((AbstractionRouter) context).getAnalyticTracker());
        }
        return null;
    }

}
