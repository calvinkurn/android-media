package com.tokopedia.digital_deals.di;

import android.content.Context;


import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital_deals.data.DealsDataStoreFactory;
import com.tokopedia.digital_deals.data.DealsRepositoryData;
import com.tokopedia.digital_deals.data.source.DealsApi;
import com.tokopedia.digital_deals.di.scope.DealsScope;
import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.GetDealsListRequestUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@Module
public class DealsModule {
    Context thisContext;

    public DealsModule(Context context) {
        thisContext = context;
    }

    @Provides
    @DealsScope
    DealsApi provideDealsApi(@DealsQualifier Retrofit retrofit) {
        return retrofit.create(DealsApi.class);
    }

    @Provides
    @DealsScope
    DealsDataStoreFactory provideDealsDataStoreFactory(DealsApi dealsApi) {
        return new DealsDataStoreFactory(dealsApi);
    }

    @Provides
    @DealsScope
    DealsRepository provideDealsRepository(DealsDataStoreFactory dealsDataStoreFactory) {
        return new DealsRepositoryData(dealsDataStoreFactory);
    }


    @Provides
    @DealsScope
    GetDealsListRequestUseCase provideGetDealsListRequestUseCase(DealsRepository dealsRepository) {
        return new GetDealsListRequestUseCase(dealsRepository);
    }


    @Provides
    @DealsQualifier
    @DealsScope
    Retrofit provideDealsRetrofit(@DealsQualifier OkHttpClient okHttpClient,
                                  Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.DEALS_DOMAIN).client(okHttpClient).build();
    }

    @DealsQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor, TkpdAuthInterceptor authInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }


    @Provides
    @DealsScope
    Context getActivityContext() {
        return thisContext;
    }
}
