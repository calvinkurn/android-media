package com.tokopedia.oms.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorResponse;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.oms.OmsModuleRouter;
import com.tokopedia.oms.data.CloudOmsDataStore;
import com.tokopedia.oms.data.OmsRepositoryData;
import com.tokopedia.oms.data.source.OmsApi;
import com.tokopedia.oms.data.source.OmsUrl;
import com.tokopedia.oms.domain.OmsInterceptor;
import com.tokopedia.oms.domain.postusecase.PostVerifyCartUseCase;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@Module
public class OmsModule {

    @Provides
    OmsApi provideOmsApi(@OmsQualifier Retrofit retrofit) {
        return retrofit.create(OmsApi.class);
    }


    @Provides
    @OmsQualifier
    Retrofit provideOmsRetrofit(@OmsQualifier OkHttpClient okHttpClient,
                                Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(OmsUrl.OMS_DOMAIN).client(okHttpClient).build();
    }

    @OmsQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor, @ApplicationContext Context context) {
        UserSession userSession=new UserSession(context);
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(new OmsInterceptor(context, (NetworkRouter) context, userSession))
                .addInterceptor(new FingerprintInterceptor((NetworkRouter) context, userSession))
                .addInterceptor(((OmsModuleRouter) context).getChuckerInterceptor())
                .build();
    }

    @Provides
    OmsRepositoryData providesOmsRepository(OmsApi api) {
        return new OmsRepositoryData(new CloudOmsDataStore(api));
    }

    @Provides
    PostVerifyCartUseCase providesPostVerifyCartUseCase(
            OmsRepositoryData omsRepositoryData) {
        return new PostVerifyCartUseCase(omsRepositoryData);

    }
}