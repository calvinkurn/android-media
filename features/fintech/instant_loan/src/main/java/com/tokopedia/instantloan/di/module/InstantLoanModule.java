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
import com.tokopedia.user.session.UserSession;

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

    @InstantLoanScope
    @Provides
    public UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}

