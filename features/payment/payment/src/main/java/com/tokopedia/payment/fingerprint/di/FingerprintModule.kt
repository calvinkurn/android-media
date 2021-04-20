package com.tokopedia.payment.fingerprint.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.payment.fingerprint.data.AccountFingerprintApi
import com.tokopedia.payment.fingerprint.data.FingerprintApi
import com.tokopedia.payment.fingerprint.data.FingerprintDataSourceCloud
import com.tokopedia.payment.fingerprint.data.FingerprintRepositoryImpl
import com.tokopedia.payment.fingerprint.domain.FingerprintRepository
import com.tokopedia.payment.fingerprint.domain.GetPostDataOtpUseCase
import com.tokopedia.payment.fingerprint.domain.PaymentFingerprintUseCase
import com.tokopedia.payment.fingerprint.domain.SaveFingerPrintUseCase
import com.tokopedia.payment.fingerprint.util.PaymentFingerprintConstant
import com.tokopedia.payment.presenter.TopPayPresenter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by zulfikarrahman on 4/2/18.
 */
@Module
class FingerprintModule {

    @FingerprintScope
    @Provides
    fun provideTopPayPresenter(saveFingerPrintUseCase: SaveFingerPrintUseCase,
                               paymentFingerprintUseCase: PaymentFingerprintUseCase,
                               getPostDataOtpUseCase: GetPostDataOtpUseCase,
                               userSession: UserSessionInterface): TopPayPresenter {
        return TopPayPresenter(saveFingerPrintUseCase, paymentFingerprintUseCase, getPostDataOtpUseCase, userSession)
    }

    @FingerprintScope
    @Provides
    fun fingerprintRepository(fingerprintDataSourceCloud: FingerprintDataSourceCloud): FingerprintRepository {
        return FingerprintRepositoryImpl(fingerprintDataSourceCloud)
    }

    @FingerprintScope
    @Provides
    fun providesFingerprintApi(@FingerprintQualifier retrofit: Retrofit): FingerprintApi {
        return retrofit.create(FingerprintApi::class.java)
    }

    @FingerprintScope
    @Provides
    fun provideAccountFingerprintApi(@AccountQualifier retrofit: Retrofit): AccountFingerprintApi {
        return retrofit.create(AccountFingerprintApi::class.java)
    }

    @AccountQualifier
    @FingerprintScope
    @Provides
    fun provideAccountRetrofit(okHttpClient: OkHttpClient,
                               retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(PaymentFingerprintConstant.ACCOUNTS_DOMAIN).client(okHttpClient).build()
    }

    @FingerprintQualifier
    @FingerprintScope
    @Provides
    fun provideFingerprintRetrofit(okHttpClient: OkHttpClient,
                                   retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(PaymentFingerprintConstant.TOP_PAY_DOMAIN).client(okHttpClient).build()
    }

    @FingerprintScope
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @FingerprintScope
    @Provides
    fun provideOkHttpClient(context: Context, httpLoggingInterceptor: HttpLoggingInterceptor, userSession: UserSessionInterface): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .addInterceptor(TkpdAuthInterceptor(context, context as NetworkRouter, userSession))
                .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @FingerprintScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    companion object {
        private const val READ_TIMEOUT = 60
        private const val WRITE_TIMEOUT = 60
    }
}