package com.tokopedia.feedplus.view.di;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerCollector;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.chuckerteam.chucker.api.RetentionManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliatecommon.data.network.TopAdsApi;
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase;
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.data.FeedAuthInterceptor;
import com.tokopedia.feedplus.data.api.FeedUrl;
import com.tokopedia.feedplus.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.feedplus.view.listener.DynamicFeedContract;
import com.tokopedia.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.feedplus.view.presenter.DynamicFeedPresenter;
import com.tokopedia.feedplus.view.presenter.FeedPlusDetailPresenter;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase;
import com.tokopedia.shop.common.data.repository.ShopCommonRepositoryImpl;
import com.tokopedia.shop.common.data.source.ShopCommonDataSource;
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.vote.di.VoteModule;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;
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
        ChuckerCollector collector = new ChuckerCollector(
                context, GlobalConfig.isAllowDebuggingTools(), RetentionManager.Period.ONE_HOUR);

        return new ChuckerInterceptor(
                context, collector, 120000L);
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
                                                     UserSessionInterface userSession) {
        return new FeedPlusDetailPresenter(getFeedsDetailUseCase,
                addWishlistUseCase,
                removeWishlistUseCase,
                userSession);
    }

    @FeedPlusScope
    @Provides
    DynamicFeedContract.Presenter provideDynamicFeedPresenter(UserSessionInterface userSession,
                                                              GetDynamicFeedUseCase getDynamicFeedUseCase,
                                                              LikeKolPostUseCase likeKolPostUseCase,
                                                              TrackAffiliateClickUseCase trackAffiliateClickUseCase) {
        return new DynamicFeedPresenter(userSession, getDynamicFeedUseCase, likeKolPostUseCase, trackAffiliateClickUseCase);
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
    ShopCommonApi provideShopCommonApi(@Named("TOME") Retrofit retrofit) {
        return retrofit.create(ShopCommonApi.class);
    }


    @FeedPlusScope
    @Provides
    ShopCommonCloudDataSource provideShopCommonCloudDataSource(ShopCommonApi shopCommonApi,
                                                               UserSessionInterface userSession) {
        return new ShopCommonCloudDataSource(shopCommonApi, userSession);
    }

    @FeedPlusScope
    @Provides
    ShopCommonDataSource provideShopCommonDataSource(ShopCommonCloudDataSource shopInfoCloudDataSource) {
        return new ShopCommonDataSource(shopInfoCloudDataSource);
    }

    @FeedPlusScope
    @Provides
    ShopCommonRepository provideShopCommonRepository(ShopCommonDataSource shopInfoDataSource) {
        return new ShopCommonRepositoryImpl(shopInfoDataSource);
    }

    @FeedPlusScope
    @Provides
    ToggleFavouriteShopUseCase provideToggleFavouriteShopUseCase( @ApplicationContext Context context) {
        return new ToggleFavouriteShopUseCase( new GraphqlUseCase(), context.getResources());
    }

    @FeedPlusScope
    @Provides
    TopAdsApi provideTopAdsApi(@Named("WS") Retrofit retrofit) {
        return retrofit.create(TopAdsApi.class);
    }

    @Provides
    @FeedPlusScope
    @Named("atcMutation")
    String provideAddToCartMutation(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.mutation_add_to_cart);
    }

    @FeedPlusScope
    @Provides
    GraphqlRepository provideGraphQlRepository(@ApplicationContext Context context) {
        GraphqlClient.init(context);
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

    @Provides
    @FeedPlusScope
    CoroutineDispatcher provideMainDispatcher() {
        return Dispatchers.getMain();
    }

    @FeedPlusScope
    @Provides
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
