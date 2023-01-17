package com.tokopedia.topads.sdk.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.topads.sdk.UrlTopAdsSdk;
import com.tokopedia.topads.sdk.base.AuthInterceptor;
import com.tokopedia.topads.sdk.domain.TopAdsWishlistService;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Author errysuprayogi on 16,July,2019
 */
@Module
public class TopAdsWishlistModule {

    @Provides
    TopAdsWishlishedUseCase topAdsWishlishedUseCase(TopAdsWishlistService topAdsWishlistService){
        return new TopAdsWishlishedUseCase(topAdsWishlistService);
    }

    @Provides
    public TopAdsWishlistService provideTopAdsWishlistService(Retrofit retrofit){
        return retrofit.create(TopAdsWishlistService.class);
    }

    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(UrlTopAdsSdk.INSTANCE.getTOP_ADS_BASE_URL()).client(okHttpClient).build();
    }

    @Provides
    public OkHttpClient provideOkHttpClient(AuthInterceptor topAdsAuthInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    public AuthInterceptor provideTopAdsAuthTempInterceptor(@ApplicationContext Context context,
                                                            NetworkRouter networkRouter,
                                                            UserSessionInterface userSession){
        return new AuthInterceptor(context, networkRouter, userSession);
    }

    @Provides
    public NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        if (context instanceof NetworkRouter) {
            return ((NetworkRouter) context);
        }
        throw new RuntimeException("Application must implement " + NetworkRouter.class.getCanonicalName());
    }

}
