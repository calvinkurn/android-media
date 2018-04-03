package com.tokopedia.payment.fingerprint.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.payment.fingerprint.data.AccountFingerprintApi;
import com.tokopedia.payment.fingerprint.data.FingerprintApi;
import com.tokopedia.payment.fingerprint.data.FingerprintDataSourceCloud;
import com.tokopedia.payment.fingerprint.data.FingerprintRepositoryImpl;
import com.tokopedia.payment.fingerprint.domain.FingerprintRepository;
import com.tokopedia.payment.fingerprint.domain.PaymentFingerprintUseCase;
import com.tokopedia.payment.fingerprint.domain.SaveFingerPrintUseCase;
import com.tokopedia.payment.fingerprint.domain.SavePublicKeyUseCase;
import com.tokopedia.payment.fingerprint.util.FingerprintConstant;
import com.tokopedia.payment.presenter.TopPayPresenter;
import com.tokopedia.payment.router.IPaymentModuleRouter;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 4/2/18.
 */

@FingerprintScope
@Module
public class FingerprintModule {

    @FingerprintScope
    @Provides
    TopPayPresenter provideTopPayPresenter(SaveFingerPrintUseCase saveFingerPrintUseCase,
                                           SavePublicKeyUseCase savePublicKeyUseCase,
                                           PaymentFingerprintUseCase paymentFingerprintUseCase,
                                           UserSession userSession){
        return new TopPayPresenter(saveFingerPrintUseCase, savePublicKeyUseCase,
                paymentFingerprintUseCase, userSession);
    };

    @FingerprintScope
    @Provides
    FingerprintRepository fingerprintRepository(FingerprintDataSourceCloud fingerprintDataSourceCloud){
        return new FingerprintRepositoryImpl(fingerprintDataSourceCloud);
    }

    @FingerprintScope
    @Provides
    FingerprintApi providesFingerprintApi(@FingerprintQualifier Retrofit retrofit){
        return retrofit.create(FingerprintApi.class);
    }

    @FingerprintScope
    @Provides
    AccountFingerprintApi provideAccountFingerprintApi(@AccountQualifier Retrofit retrofit){
        return retrofit.create(AccountFingerprintApi.class);
    }

    @AccountQualifier
    @FingerprintScope
    @Provides
    public Retrofit provideAccountRetrofit(OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(FingerprintConstant.ACCOUNTS_DOMAIN).client(okHttpClient).build();
    }

    @FingerprintQualifier
    @FingerprintScope
    @Provides
    public Retrofit provideFingerprintRetrofit(OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(FingerprintConstant.TOP_PAY_DOMAIN).client(okHttpClient).build();
    }

    @FingerprintScope
    @Provides
    public OkHttpClient provideOkHttpClient(TkpdAuthInterceptor tkpdAuthInterceptor){
        return new OkHttpClient.Builder()
                .addInterceptor(tkpdAuthInterceptor)
                .build();
    }
}
