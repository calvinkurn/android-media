package com.tokopedia.recentview.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.recentview.data.api.RecentViewApi;
import com.tokopedia.recentview.data.api.RecentViewUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.data.interceptor.MojitoInterceptor;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by milhamj on 15/08/18.
 */

@Module
public class RecentViewModule {

    @Provides
    @RecentViewQualifier
    ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools());
    }

    @RecentViewScope
    @Provides
    @RecentViewQualifier
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }

    @RecentViewScope
    @Provides
    MojitoInterceptor provideMojitoInterceptor(@ApplicationContext Context context,
                                               AbstractionRouter abstractionRouter) {
        return new MojitoInterceptor(context, abstractionRouter);
    }

    @RecentViewScope
    @Provides
    @RecentViewQualifier
    OkHttpClient provideMojitoOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                           @RecentViewQualifier OkHttpRetryPolicy retryPolicy,
                                           @RecentViewQualifier ChuckInterceptor chuckInterceptor,
                                           HeaderErrorResponseInterceptor errorResponseInterceptor,
                                           MojitoInterceptor mojitoInterceptor) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(mojitoInterceptor)
                .addInterceptor(errorResponseInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor);
            clientBuilder.addInterceptor(chuckInterceptor);
        }

        return clientBuilder.build();
    }

    @RecentViewScope
    @Provides
    RecentViewApi provideRecentProductService(Retrofit.Builder builder,
                                              @RecentViewQualifier OkHttpClient okHttpClient) {
        return builder.baseUrl(RecentViewUrl.MOJITO_DOMAIN)
                .client(okHttpClient)
                .build()
                .create(RecentViewApi.class);
    }

    @RecentViewScope
    @Provides
    AddWishListUseCase providesTkpTkpdAddWishListUseCase(@ApplicationContext Context context){
        return new AddWishListUseCase(context);
    }

    @RecentViewScope
    @Provides
    RemoveWishListUseCase providesTkpdRemoveWishListUseCase(@ApplicationContext Context context){
        return new RemoveWishListUseCase(context);
    }

    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
