package com.tokopedia.logisticaddaddress.di.districtrecommendation;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerCollector;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ActivityScope;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.logisticCommon.data.converter.GeneratedHostConverter;
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery;
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase;
import com.tokopedia.logisticaddaddress.data.repository.DistrictRecommendationRepository;
import com.tokopedia.logisticaddaddress.data.service.KeroApi;
import com.tokopedia.logisticaddaddress.data.source.DistrictRecommendationDataStore;
import com.tokopedia.logisticaddaddress.domain.executor.MainSchedulerProvider;
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider;
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationEntityMapper;
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper;
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendation;
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRequestUseCase;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomPresenter;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
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
    @ActivityScope
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(
                NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY
        );
    }

    @Provides
    @ActivityScope
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter) context.getApplicationContext();
    }

    @Provides
    @ActivityScope
    FingerprintInterceptor provideFingerPrintInterceptor(NetworkRouter networkRouter,
                                                         UserSessionInterface userSessionInterface) {
        return new FingerprintInterceptor(networkRouter, userSessionInterface);
    }

    @Provides
    @ActivityScope
    TkpdAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                   NetworkRouter networkRouter,
                                                   UserSessionInterface userSessionInterface) {
        return new TkpdAuthInterceptor(context, networkRouter, userSessionInterface);
    }

    @Provides
    @ActivityScope
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
    @ActivityScope
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
    @ActivityScope
    DistrictRecommendationDataStore provideDistrictRecommendationDataStore(KeroApi keroApi) {
        return new DistrictRecommendationDataStore(keroApi);
    }

    @Provides
    @ActivityScope
    DistrictRecommendationEntityMapper provideEntityMapper() {
        return new DistrictRecommendationEntityMapper();
    }

    @Provides
    @ActivityScope
    DistrictRecommendationRepository provideDistrictRecommendationRepository(
            DistrictRecommendationDataStore districtRecommendationDataStore,
            DistrictRecommendationEntityMapper districtRecommendationEntityMapper
    ) {
        return new DistrictRecommendationRepository(districtRecommendationDataStore,
                districtRecommendationEntityMapper);
    }

    @Provides
    @ActivityScope
    GetDistrictRequestUseCase provideGetDistrictRequestUseCase(
            DistrictRecommendationRepository districtRecommendationRepository
    ) {
        return new GetDistrictRequestUseCase(districtRecommendationRepository);
    }

    @Provides
    @ActivityScope
    DiscomContract.Presenter provideDistrictRecommendationPresenter(
            GetDistrictRequestUseCase getDistrictRequestUseCase,
            RevGeocodeUseCase revGeocodeUseCase,
            GetDistrictRecommendation getDistrictRecommendation,
            DistrictRecommendationMapper mapper) {
        return new DiscomPresenter(getDistrictRequestUseCase, revGeocodeUseCase, getDistrictRecommendation, mapper);
    }

    @Provides
    @ActivityScope
    SchedulerProvider provideSchedulerProvider() {
        return new MainSchedulerProvider();
    }

    @Provides
    @IntoMap
    @StringKey(RawQueryConstant.GET_DISTRICT_RECOMMENDATION)
    String provideQueryDiscom() {
        return KeroLogisticQuery.INSTANCE.getDistrict_recommendation();
    }


}
