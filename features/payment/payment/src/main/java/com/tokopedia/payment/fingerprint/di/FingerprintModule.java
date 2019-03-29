package com.tokopedia.payment.fingerprint.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.payment.fingerprint.data.AccountFingerprintApi;
import com.tokopedia.payment.fingerprint.data.FingerprintApi;
import com.tokopedia.payment.fingerprint.data.FingerprintDataSourceCloud;
import com.tokopedia.payment.fingerprint.data.FingerprintRepositoryImpl;
import com.tokopedia.payment.fingerprint.domain.FingerprintRepository;
import com.tokopedia.payment.fingerprint.domain.GetPostDataOtpUseCase;
import com.tokopedia.payment.fingerprint.domain.PaymentFingerprintUseCase;
import com.tokopedia.payment.fingerprint.domain.SaveFingerPrintUseCase;
import com.tokopedia.payment.fingerprint.domain.SavePublicKeyUseCase;
import com.tokopedia.payment.fingerprint.util.PaymentFingerprintConstant;
import com.tokopedia.payment.presenter.TopPayPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

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

    public static final int READ_TIMEOUT = 60;
    public static final int WRITE_TIMEOUT = 60;

    @FingerprintScope
    @Provides
    TopPayPresenter provideTopPayPresenter(SaveFingerPrintUseCase saveFingerPrintUseCase,
                                           SavePublicKeyUseCase savePublicKeyUseCase,
                                           PaymentFingerprintUseCase paymentFingerprintUseCase,
                                           GetPostDataOtpUseCase getPostDataOtpUseCase,
                                           UserSessionInterface userSession) {
        return new TopPayPresenter(saveFingerPrintUseCase, savePublicKeyUseCase,
                paymentFingerprintUseCase, getPostDataOtpUseCase, userSession);
    }

    ;

    @FingerprintScope
    @Provides
    FingerprintRepository fingerprintRepository(FingerprintDataSourceCloud fingerprintDataSourceCloud) {
        return new FingerprintRepositoryImpl(fingerprintDataSourceCloud);
    }

    @FingerprintScope
    @Provides
    FingerprintApi providesFingerprintApi(@FingerprintQualifier Retrofit retrofit) {
        return retrofit.create(FingerprintApi.class);
    }

    @FingerprintScope
    @Provides
    AccountFingerprintApi provideAccountFingerprintApi(@AccountQualifier Retrofit retrofit) {
        return retrofit.create(AccountFingerprintApi.class);
    }

    @AccountQualifier
    @FingerprintScope
    @Provides
    Retrofit provideAccountRetrofit(OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(PaymentFingerprintConstant.ACCOUNTS_DOMAIN).client(okHttpClient).build();
    }

    @FingerprintQualifier
    @FingerprintScope
    @Provides
    Retrofit provideFingerprintRetrofit(OkHttpClient okHttpClient,
                                        Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(PaymentFingerprintConstant.TOP_PAY_DOMAIN).client(okHttpClient).build();
    }

    @FingerprintScope
    @Provides
    Context provideContext(@ApplicationContext Context context) {
        return context;
    }

    @FingerprintScope
    @Provides
    OkHttpClient provideOkHttpClient(Context context, HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(
                        new TkpdAuthInterceptor(
                                context,
                                (NetworkRouter) context,
                                new com.tokopedia.user.session.UserSession(context)
                        )
                )
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }

    @FingerprintScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
