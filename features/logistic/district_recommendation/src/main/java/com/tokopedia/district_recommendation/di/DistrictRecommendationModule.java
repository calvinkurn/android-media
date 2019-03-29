package com.tokopedia.district_recommendation.di;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.district_recommendation.data.mapper.DistrictRecommendationEntityMapper;
import com.tokopedia.district_recommendation.data.repository.DistrictRecommendationRepository;
import com.tokopedia.district_recommendation.data.repository.ShopAddressRepository;
import com.tokopedia.district_recommendation.data.service.KeroApi;
import com.tokopedia.district_recommendation.data.service.MyShopAddressApi;
import com.tokopedia.district_recommendation.data.source.DistrictRecommendationDataStore;
import com.tokopedia.district_recommendation.data.source.ShopAddressDataSource;
import com.tokopedia.district_recommendation.domain.usecase.GetDistrictRequestUseCase;
import com.tokopedia.district_recommendation.domain.usecase.GetShopAddressUseCase;
import com.tokopedia.district_recommendation.view.AddressViewModelMapper;
import com.tokopedia.district_recommendation.view.DistrictRecommendationContract;
import com.tokopedia.district_recommendation.view.DistrictRecommendationPresenter;
import com.tokopedia.logisticdata.data.converter.GeneratedHostConverter;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
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
            Interceptor chuckInterceptor = new ChuckInterceptor(context)
                    .showNotification(abstractionRouter.isAllowLogOnChuckInterceptorNotification());
            builder.addInterceptor(chuckInterceptor);
        }
        return builder.build();
    }

    @Provides
    @DistrictRecommendationScope
    KeroApi provideKeroApi(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.KERO_DOMAIN)
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
    AddressViewModelMapper provideViewModelMapper() {
        return new AddressViewModelMapper();
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
    GetShopAddressUseCase provideGetShopAddressRequestUseCase(
            ShopAddressRepository shopAddressRepository
    ) {
        return new GetShopAddressUseCase(shopAddressRepository);
    }

    @Provides
    @DistrictRecommendationScope
    DistrictRecommendationContract.Presenter provideDistrictRecommendationPresenter(
            GetDistrictRequestUseCase getDistrictRequestUseCase,
            AddressViewModelMapper addressViewModelMapper) {
        return new DistrictRecommendationPresenter(getDistrictRequestUseCase, addressViewModelMapper);
    }

}
