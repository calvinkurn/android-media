package com.tokopedia.instantloan.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.instantloan.di.scope.InstantLoanScope;
import com.tokopedia.instantloan.domain.interactor.GetBannersUserCase;
import com.tokopedia.instantloan.domain.interactor.GetLoanProfileStatusUseCase;
import com.tokopedia.instantloan.domain.interactor.PostPhoneDataUseCase;
import com.tokopedia.instantloan.network.InstantLoanAuthInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tokopedia.instantloan.network.InstantLoanUrl.BaseUrl.WEB_DOMAIN;

/**
 * Created by lavekush on 19/03/18.
 */
@InstantLoanScope
@Module
public class InstantLoanModule {

    /*@InstantLoanQualifier
    @InstantLoanScope
    @Provides
    public Retrofit provideRetrofit(@InstantLoanQualifier OkHttpClient okHttpClient) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        return retrofitBuilder.baseUrl(WEB_DOMAIN)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();
    }

    @InstantLoanQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor,
                                            @ChuckQualifier Interceptor chuckInterceptor,
                                            InstantLoanAuthInterceptor authInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chuckInterceptor)
                .build();
    }

    @Provides
    public InstantLoanChuckRouter provideInstantLoanRouter(@ApplicationContext Context context) {
        if (context instanceof InstantLoanChuckRouter) {
            return ((InstantLoanChuckRouter) context);
        }
        throw new RuntimeException("App should implement " + InstantLoanChuckRouter.class.getSimpleName());
    }

    @Provides
    @ChuckQualifier
    public Interceptor provideChuckInterceptor(InstantLoanChuckRouter router) {
        return router.getChuckInterceptor();
    }*/

    @Provides
    public GetLoanProfileStatusUseCase provideGetLoanProfileStatusUseCase(InstantLoanAuthInterceptor instantLoanAuthInterceptor, @ApplicationContext Context context) {
        return new GetLoanProfileStatusUseCase(instantLoanAuthInterceptor, context);
    }

    @Provides
    public GetBannersUserCase provideGetBannersUseCase(InstantLoanAuthInterceptor instantLoanAuthInterceptor, @ApplicationContext Context context) {
        return new GetBannersUserCase(instantLoanAuthInterceptor, context);
    }

    @Provides
    public PostPhoneDataUseCase providePostPhoneDataUseCase(InstantLoanAuthInterceptor instantLoanAuthInterceptor, @ApplicationContext Context context) {
        return new PostPhoneDataUseCase(instantLoanAuthInterceptor, context);
    }
}

