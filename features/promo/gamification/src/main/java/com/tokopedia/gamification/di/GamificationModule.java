package com.tokopedia.gamification.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.gamification.GamificationRouter;
import com.tokopedia.gamification.GamificationUrl;
import com.tokopedia.gamification.data.GamificationApi;
import com.tokopedia.gamification.data.GamificationRepository;
import com.tokopedia.gamification.domain.GetCrackResultEggUseCase;
import com.tokopedia.gamification.domain.GetTokenTokopointsUseCase;
import com.tokopedia.gamification.network.GamificationAuthInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by nabillasabbaha on 3/28/18.
 */
@Module
public class GamificationModule {

    @GamificationScope
    @Provides
    OkHttpClient provideOkHttpClient(GamificationAuthInterceptor gamificationAuthInterceptor,
                                     @GamificationChuckQualifier Interceptor chuckInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(gamificationAuthInterceptor)
                .addInterceptor(chuckInterceptor)
                .build();
    }

    @GamificationScope
    @GamificationChuckQualifier
    @Provides
    Interceptor provideChuckInterceptor(@ApplicationContext Context context) {
        if (context instanceof GamificationRouter) {
            return ((GamificationRouter) context).getChuckInterceptor();
        }
        throw new RuntimeException("App should implement " + GamificationRouter.class.getSimpleName());
    }

    @GamificationScope
    @Provides
    Retrofit provideRetrofit(Retrofit.Builder retrofitBuilder, OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(GamificationUrl.GQL_BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    GamificationApi provideTokoCashApi(Retrofit retrofit) {
        return retrofit.create(GamificationApi.class);
    }

    @Provides
    GetTokenTokopointsUseCase provideGetTokenTokopointsUseCase(GamificationRepository gamificationRepository) {
        return new GetTokenTokopointsUseCase(gamificationRepository);
    }

    @Provides
    GetCrackResultEggUseCase provideGetCrackResultEggUseCase(GamificationRepository gamificationRepository) {
        return new GetCrackResultEggUseCase(gamificationRepository);
    }
}
