package com.tokopedia.tkpdreactnative.react.fingerprint.di;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.AccountFingerprintApi;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.FingerprintApi;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.FingerprintDataSourceCloud;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.FingerprintRepositoryImpl;
import com.tokopedia.tkpdreactnative.react.fingerprint.domain.FingerprintRepository;
import com.tokopedia.tkpdreactnative.react.fingerprint.domain.SaveFingerPrintUseCase;
import com.tokopedia.tkpdreactnative.react.fingerprint.utils.FingerprintConstantRegister;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.presenter.SaveFingerPrintPresenter;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.view.presenter.SetSingleAuthPaymentPresenter;
import com.tokopedia.tkpdreactnative.router.ReactNativeRouter;

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
    SaveFingerPrintPresenter provideSaveFingerPrintPresenter(SaveFingerPrintUseCase saveFingerPrintUseCase,
                                                    UserSession userSession){
        return new SaveFingerPrintPresenter(userSession, saveFingerPrintUseCase);
    };

    @FingerprintScope
    @Provides
    SetSingleAuthPaymentPresenter provideSetSingleAuthPaymentPresenter(ReactNativeRouter reactNativeRouter,
                                                                       UserSession userSession) {
        return new SetSingleAuthPaymentPresenter(reactNativeRouter, userSession);
    }

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
        return retrofitBuilder.baseUrl(FingerprintConstantRegister.ACCOUNTS_DOMAIN).client(okHttpClient).build();
    }

    @FingerprintQualifier
    @FingerprintScope
    @Provides
    public Retrofit provideFingerprintRetrofit(OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(FingerprintConstantRegister.TOP_PAY_DOMAIN).client(okHttpClient).build();
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
