package com.tokopedia.gamification.di;

import com.tokopedia.gamification.GamificationUrl;
import com.tokopedia.gamification.data.GamificationApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by nabillasabbaha on 3/28/18.
 */
@Module
public class GamificationModule {

    @GamificationScope
    @Provides
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().build();
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
}
