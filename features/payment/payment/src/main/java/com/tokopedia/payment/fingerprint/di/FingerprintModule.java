package com.tokopedia.payment.fingerprint.di;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.payment.fingerprint.data.AccountFingerprintApi;
import com.tokopedia.payment.fingerprint.data.FingerprintApi;
import com.tokopedia.payment.fingerprint.data.FingerprintDataSourceCloud;
import com.tokopedia.payment.fingerprint.data.FingerprintRepositoryImpl;
import com.tokopedia.payment.fingerprint.domain.FingerprintRepository;
import com.tokopedia.payment.fingerprint.domain.GetPostDataOtpUseCase;
import com.tokopedia.payment.fingerprint.domain.PaymentFingerprintUseCase;
import com.tokopedia.payment.fingerprint.domain.SaveFingerPrintUseCase;
import com.tokopedia.payment.fingerprint.domain.SavePublicKeyUseCase;
import com.tokopedia.payment.fingerprint.util.FingerprintConstant;
import com.tokopedia.payment.presenter.TopPayPresenter;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 4/2/18.
 */

@FingerprintScope
@Module
public class FingerprintModule {

    public static final int READ_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 30;

    @FingerprintScope
    @Provides
    TopPayPresenter provideTopPayPresenter(SaveFingerPrintUseCase saveFingerPrintUseCase,
                                           SavePublicKeyUseCase savePublicKeyUseCase,
                                           PaymentFingerprintUseCase paymentFingerprintUseCase,
                                           GetPostDataOtpUseCase getPostDataOtpUseCase,
                                           UserSession userSession){
        return new TopPayPresenter(saveFingerPrintUseCase, savePublicKeyUseCase,
                paymentFingerprintUseCase, getPostDataOtpUseCase, userSession);
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
    public OkHttpClient provideOkHttpClient(TkpdAuthInterceptor tkpdAuthInterceptor, HttpLoggingInterceptor httpLoggingInterceptor){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(tkpdAuthInterceptor)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        if(GlobalConfig.isAllowDebuggingTools()){
            builder.addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }
}
