package com.tokopedia.feedplus.view.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.feedplus.FeedModuleRouter;
import com.tokopedia.feedplus.data.FeedAuthInterceptor;
import com.tokopedia.feedplus.data.api.FeedApi;
import com.tokopedia.feedplus.data.api.FeedUrl;
import com.tokopedia.feedplus.data.factory.FeedFactory;
import com.tokopedia.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.data.repository.FeedRepositoryImpl;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.feedplus.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.feedplus.view.presenter.FeedPlusDetailPresenter;
import com.tokopedia.shop.common.data.repository.ShopCommonRepositoryImpl;
import com.tokopedia.shop.common.data.source.ShopCommonDataSource;
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonWSApi;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;
import com.tokopedia.vote.di.VoteModule;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by nisie on 5/15/17.
 */

@Module(includes = {VoteModule.class})
public class FeedPlusModule {

    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    private static final String NAME_CLOUD = "CLOUD";
    private static final String NAME_LOCAL = "LOCAL";

    @FeedPlusScope
    @Provides
    OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor
                                             httpLoggingInterceptor,
                                     @FeedPlusQualifier OkHttpRetryPolicy retryPolicy,
                                     @FeedPlusChuckQualifier Interceptor chuckInterceptor,
                                     FeedAuthInterceptor feedAuthInterceptor) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(feedAuthInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor);
            clientBuilder.addInterceptor(chuckInterceptor);
        }

        return clientBuilder.build();
    }

    @FeedPlusScope
    @FeedPlusQualifier
    @Provides
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY);
    }

    @FeedPlusScope
    @FeedPlusChuckQualifier
    @Provides
    Interceptor provideChuckInterceptory(@ApplicationContext Context context) {
        if (context instanceof FeedModuleRouter) {
            return ((FeedModuleRouter) context).getChuckInterceptor();
        }
        throw new RuntimeException("App should implement " + FeedModuleRouter.class.getSimpleName());
    }

    @FeedPlusScope
    @Provides
    FeedApi provideFeedApi(Retrofit.Builder retrofitBuilder,
                           OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(FeedUrl.GRAPHQL_DOMAIN)
                .client(okHttpClient)
                .build()
                .create(FeedApi.class);
    }

    @FeedPlusScope
    @Provides
    FeedRepository provideFeedRepository(FeedFactory feedFactory) {
        return new FeedRepositoryImpl(feedFactory);
    }

    @FeedPlusScope
    @Provides
    FeedFactory provideFeedFactory(@ApplicationContext Context context,
                                   FeedApi feedApi,
                                   FeedListMapper feedListMapper,
                                   @Named(NAME_LOCAL) FeedResultMapper feedResultMapperLocal,
                                   @Named(NAME_CLOUD) FeedResultMapper feedResultMapperCloud,
                                   CacheManager globalCacheManager) {
        return new FeedFactory(
                context,
                feedApi,
                feedListMapper,
                feedResultMapperLocal,
                feedResultMapperCloud,
                globalCacheManager
        );
    }

    @FeedPlusScope
    @Named(NAME_LOCAL)
    @Provides
    FeedResultMapper provideLocalFeedResultMapper() {
        return new FeedResultMapper(FeedResult.SOURCE_LOCAL);
    }

    @FeedPlusScope
    @Named(NAME_CLOUD)
    @Provides
    FeedResultMapper provideCloudFeedResultMapper() {
        return new FeedResultMapper(FeedResult.SOURCE_CLOUD);
    }

    @FeedPlusScope
    @Provides
    AddWishListUseCase providesTkpTkpdAddWishListUseCase(@ApplicationContext Context context) {
        return new AddWishListUseCase(context);
    }

    @FeedPlusScope
    @Provides
    RemoveWishListUseCase providesTkpdRemoveWishListUseCase(@ApplicationContext Context context) {
        return new RemoveWishListUseCase(context);
    }

    @FeedPlusScope
    @Provides
    FeedPlusDetail.Presenter FeedPlusDetailPresenter(GetFeedsDetailUseCase getFeedsDetailUseCase,
                                                     AddWishListUseCase addWishlistUseCase,
                                                     RemoveWishListUseCase removeWishlistUseCase,
                                                     UserSession userSession) {
        return new FeedPlusDetailPresenter(getFeedsDetailUseCase,
                addWishlistUseCase,
                removeWishlistUseCase,
                userSession);
    }

    //SHOP COMMON
    //TODO : Change to when shop common component is provided

    @FeedPlusScope
    @Named("WS")
    @Provides
    Retrofit provideWsRetrofitDomain(OkHttpClient okHttpClient,
                                     Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(FeedUrl.BASE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @FeedPlusScope
    @Named("TOME")
    @Provides
    Retrofit provideTomeRetrofitDomain(OkHttpClient okHttpClient,
                                       Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(FeedUrl.TOME_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @FeedPlusScope
    @Provides
    public ShopCommonWSApi provideShopCommonWsApi(@Named("WS") Retrofit retrofit) {
        return retrofit.create(ShopCommonWSApi.class);
    }

    @FeedPlusScope
    @Provides
    public ShopCommonApi provideShopCommonApi(@Named("TOME") Retrofit retrofit) {
        return retrofit.create(ShopCommonApi.class);
    }


    @FeedPlusScope
    @Provides
    public ShopCommonCloudDataSource provideShopCommonCloudDataSource(ShopCommonApi shopCommonApi,
                                                                      ShopCommonWSApi shopCommonWS4Api,
                                                                      com.tokopedia.abstraction.common.data.model.session.UserSession userSession) {
        return new ShopCommonCloudDataSource(shopCommonApi, shopCommonWS4Api, userSession);
    }

    @FeedPlusScope
    @Provides
    public ShopCommonDataSource provideShopCommonDataSource(ShopCommonCloudDataSource shopInfoCloudDataSource) {
        return new ShopCommonDataSource(shopInfoCloudDataSource);
    }

    @FeedPlusScope
    @Provides
    public ShopCommonRepository provideShopCommonRepository(ShopCommonDataSource shopInfoDataSource) {
        return new ShopCommonRepositoryImpl(shopInfoDataSource);
    }

    @FeedPlusScope
    @Provides
    ToggleFavouriteShopUseCase provideToggleFavouriteShopUseCase(ShopCommonRepository shopCommonRepository) {
        return new ToggleFavouriteShopUseCase(shopCommonRepository);
    }


}
