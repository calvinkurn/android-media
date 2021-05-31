package com.tokopedia.logisticaddaddress.di;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerCollector;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase;
import com.tokopedia.logisticaddaddress.data.repository.DistrictRecommendationRepository;
import com.tokopedia.logisticaddaddress.data.repository.ShopAddressRepository;
import com.tokopedia.logisticaddaddress.data.service.KeroApi;
import com.tokopedia.logisticaddaddress.data.service.MyShopAddressApi;
import com.tokopedia.logisticaddaddress.data.source.DistrictRecommendationDataStore;
import com.tokopedia.logisticaddaddress.data.source.ShopAddressDataSource;
import com.tokopedia.logisticaddaddress.domain.executor.MainSchedulerProvider;
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider;
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationEntityMapper;
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper;
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecomToken;
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendation;
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRequestUseCase;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomPresenter;
import com.tokopedia.logisticCommon.data.converter.GeneratedHostConverter;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;
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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Irfan Khoirul on 20/11/17.
 */

@Module
public class DistrictRecommendationModule {

    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 0;

    public DistrictRecommendationModule() {

    }

    @Provides
    @DistrictRecommendationScope
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    @DistrictRecommendationScope
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(
                NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY
        );
    }

    @Provides
    @DistrictRecommendationScope
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter) context.getApplicationContext();
    }

    @Provides
    @DistrictRecommendationScope
    FingerprintInterceptor provideFingerPrintInterceptor(NetworkRouter networkRouter,
                                                         UserSessionInterface userSessionInterface) {
        return new FingerprintInterceptor(networkRouter, userSessionInterface);
    }

    @Provides
    @DistrictRecommendationScope
    TkpdAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                   NetworkRouter networkRouter,
                                                   UserSessionInterface userSessionInterface) {
        return new TkpdAuthInterceptor(context, networkRouter, userSessionInterface);
    }

    @Provides
    @DistrictRecommendationScope
    OkHttpClient provideKeroOkHttpClient(@ApplicationContext Context context,
                                         OkHttpRetryPolicy okHttpRetryPolicy,
                                         TkpdAuthInterceptor tkpdAuthInterceptor,
                                         FingerprintInterceptor fingerprintInterceptor) {

        AbstractionRouter abstractionRouter = ((AbstractionRouter) context);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdAuthInterceptor);
        if (GlobalConfig.isAllowDebuggingTools()) {
            ChuckerCollector collector = new ChuckerCollector(
                    context, abstractionRouter.isAllowLogOnChuckInterceptorNotification());

            Interceptor chuckInterceptor = new ChuckerInterceptor(context, collector);
            builder.addInterceptor(chuckInterceptor);
        }
        return builder.build();
    }

    @Provides
    @DistrictRecommendationScope
    KeroApi provideKeroApi(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TokopediaUrl.Companion.getInstance().getKERO())
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(KeroApi.class);
    }

    @Provides
    @DistrictRecommendationScope
    MyShopAddressApi provideMyShopAddressApi(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.Shop.URL_MY_SHOP_ADDRESS)
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(MyShopAddressApi.class);
    }

    @Provides
    @DistrictRecommendationScope
    DistrictRecommendationDataStore provideDistrictRecommendationDataStore(KeroApi keroApi) {
        return new DistrictRecommendationDataStore(keroApi);
    }

    @Provides
    @DistrictRecommendationScope
    DistrictRecommendationEntityMapper provideEntityMapper() {
        return new DistrictRecommendationEntityMapper();
    }

    @Provides
    @DistrictRecommendationScope
    DistrictRecommendationRepository provideDistrictRecommendationRepository(
            DistrictRecommendationDataStore districtRecommendationDataStore,
            DistrictRecommendationEntityMapper districtRecommendationEntityMapper
    ) {
        return new DistrictRecommendationRepository(districtRecommendationDataStore,
                districtRecommendationEntityMapper);
    }

    @Provides
    @DistrictRecommendationScope
    GetDistrictRequestUseCase provideGetDistrictRequestUseCase(
            DistrictRecommendationRepository districtRecommendationRepository
    ) {
        return new GetDistrictRequestUseCase(districtRecommendationRepository);
    }

    @Provides
    @DistrictRecommendationScope
    ShopAddressDataSource provideShopAddressDataStore(MyShopAddressApi myShopAddressApi) {
        return new ShopAddressDataSource(myShopAddressApi);
    }

    @Provides
    @DistrictRecommendationScope
    ShopAddressRepository provideShopAddressRepository(ShopAddressDataSource shopAddressDataSource) {
        return new ShopAddressRepository(shopAddressDataSource);
    }

    @Provides
    @DistrictRecommendationScope
    GetDistrictRecomToken provideGetDistrictRecomTokenUseCase(
            ShopAddressRepository shopAddressRepository
    ) {
        return new GetDistrictRecomToken(shopAddressRepository);
    }

    @Provides
    @DistrictRecommendationScope
    DiscomContract.Presenter provideDistrictRecommendationPresenter(
            GetDistrictRequestUseCase getDistrictRequestUseCase,
            RevGeocodeUseCase revGeocodeUseCase,
            GetDistrictRecommendation getDistrictRecommendation,
            DistrictRecommendationMapper mapper) {
        return new DiscomPresenter(getDistrictRequestUseCase, revGeocodeUseCase, getDistrictRecommendation, mapper);
    }

    @Provides
    @DistrictRecommendationScope
    SchedulerProvider provideSchedulerProvider() {
        return new MainSchedulerProvider();
    }

}
