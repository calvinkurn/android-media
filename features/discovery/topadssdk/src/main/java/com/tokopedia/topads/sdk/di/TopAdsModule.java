package com.tokopedia.topads.sdk.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.topads.sdk.base.AuthInterceptor;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.TopAdsWishlistService;
import com.tokopedia.topads.sdk.domain.interactor.OpenTopAdsUseCase;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsGqlUseCase;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsUseCase;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase;
import com.tokopedia.topads.sdk.presenter.BannerAdsPresenter;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.topads.sdk.utils.CacheHandler;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@Module
public class TopAdsModule {

    @TopAdsScope
    @Provides
    public TopAdsGqlUseCase provideTopAdsGqlUseCase(@ApplicationContext Context context){
        return new TopAdsGqlUseCase(context);
    }

    @TopAdsScope
    @Provides
    public NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        if (context instanceof NetworkRouter) {
            return ((NetworkRouter) context);
        }
        throw new RuntimeException("Application must implement " + NetworkRouter.class.getCanonicalName());
    }

    @TopAdsScope
    @Provides
    TopAdsWishlishedUseCase topAdsWishlishedUseCase(TopAdsWishlistService topAdsWishlistService){
        return new TopAdsWishlishedUseCase(topAdsWishlistService);
    }

    @TopAdsScope
    @Provides
    public TopAdsWishlistService provideTopAdsWishlistService(Retrofit retrofit){
        return retrofit.create(TopAdsWishlistService.class);
    }

    @TopAdsScope
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(Config.TOPADS_BASE_URL).client(okHttpClient).build();
    }

    @TopAdsScope
    @Provides
    public OkHttpClient provideOkHttpClient(AuthInterceptor topAdsAuthInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @TopAdsScope
    @Provides
    public AuthInterceptor provideTopAdsAuthTempInterceptor(@ApplicationContext Context context,
                                                            NetworkRouter networkRouter,
                                                            UserSession userSession){
        return new AuthInterceptor(context, networkRouter, userSession);
    }

    @TopAdsScope
    @Provides
    UserSession provideUserSession(@ApplicationContext Context context){
        return new UserSession(context);
    }

    @TopAdsScope
    @Provides
    BannerAdsPresenter provideBannerAdsPresenter(@ApplicationContext Context context){
        return new BannerAdsPresenter(context);
    }

    @TopAdsScope
    @Provides
    TopAdsPresenter provideTopAdsPresenter(@ApplicationContext Context context){
        return new TopAdsPresenter(context);
    }

    @TopAdsScope
    @Provides
    TopAdsParams provideTopAdsParams(){
        return new TopAdsParams();
    }

    @TopAdsScope
    @Provides
    CacheHandler provideCacheHandler(@ApplicationContext Context context){
        return new CacheHandler(context, CacheHandler.TOP_ADS_CACHE);
    }

    @TopAdsScope
    @Provides
    TopAdsUseCase provideTopAdsUseCase(@ApplicationContext Context context){
        return new TopAdsUseCase(context);
    }

    @TopAdsScope
    @Provides
    OpenTopAdsUseCase provideOpenTopAdsUseCase(@ApplicationContext Context context){
        return new OpenTopAdsUseCase(context);
    }

    @TopAdsScope
    @Provides
    AddWishListUseCase addWishListUseCase(@ApplicationContext Context context){
        return new AddWishListUseCase(context);
    }

    @TopAdsScope
    @Provides
    RemoveWishListUseCase removeWishListUseCase(@ApplicationContext Context context){
        return new RemoveWishListUseCase(context);
    }
}
