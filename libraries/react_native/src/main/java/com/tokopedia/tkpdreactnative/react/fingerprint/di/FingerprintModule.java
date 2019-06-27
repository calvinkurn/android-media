package com.tokopedia.tkpdreactnative.react.fingerprint.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.tkpdreactnative.react.common.data.PreferenceRepository;
import com.tokopedia.tkpdreactnative.react.common.data.PreferenceRepositoryImpl;
import com.tokopedia.tkpdreactnative.react.common.data.source.DataSourcePreference;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.AccountFingerprintApi;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.FingerprintApi;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.FingerprintDataSourceCloud;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.FingerprintRepositoryImpl;
import com.tokopedia.tkpdreactnative.react.fingerprint.domain.FingerprintGetPreferenceUseCase;
import com.tokopedia.tkpdreactnative.react.fingerprint.domain.FingerprintRepository;
import com.tokopedia.tkpdreactnative.react.fingerprint.domain.FingerprintSavePreferenceUseCase;
import com.tokopedia.tkpdreactnative.react.fingerprint.domain.SaveFingerPrintUseCase;
import com.tokopedia.tkpdreactnative.react.fingerprint.utils.FingerprintConstantRegister;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.presenter.FingerprintConfirmationPresenter;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.presenter.SaveFingerPrintPresenter;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.domain.SinglePaymentGetPreferenceUseCase;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.domain.SinglePaymentSavePreferenceUseCase;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.view.presenter.SetSingleAuthPaymentPresenter;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.view.presenter.SinglePaymentPresenter;
import com.tokopedia.tkpdreactnative.router.ReactNativeRouter;
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
    public static final String FINGERPRINT_PREFERENCE = "fingerprint_preference";

    @FingerprintScope
    @Provides
    SaveFingerPrintPresenter provideSaveFingerPrintPresenter(SaveFingerPrintUseCase saveFingerPrintUseCase,
                                                             UserSessionInterface userSession){
        return new SaveFingerPrintPresenter(userSession, saveFingerPrintUseCase);
    };

    @FingerprintScope
    @Provides
    SetSingleAuthPaymentPresenter provideSetSingleAuthPaymentPresenter(ReactNativeRouter reactNativeRouter,
                                                                       UserSessionInterface userSession) {
        return new SetSingleAuthPaymentPresenter(reactNativeRouter, userSession);
    }

    @FingerprintScope
    @Provides
    ReactNativeRouter provideReactNativeRouter(@ApplicationContext Context context) {
        if(context instanceof ReactNativeRouter){
            return (ReactNativeRouter) context;
        }else{
            return null;
        }
    }

    @FingerprintScope
    @Provides
    SinglePaymentPresenter provideSinglePaymentPresenter(SinglePaymentSavePreferenceUseCase singlePaymentSavePreferenceUseCase,
                                                                   SinglePaymentGetPreferenceUseCase singlePaymentGetPreferenceUseCase){
        return new SinglePaymentPresenter(singlePaymentSavePreferenceUseCase, singlePaymentGetPreferenceUseCase);
    };

    @FingerprintScope
    @Provides
    FingerprintConfirmationPresenter provideFingerprintConfirmationPresenter(FingerprintSavePreferenceUseCase fingerprintSavePreferenceUseCase,
                                                                     FingerprintGetPreferenceUseCase fingerprintGetPreferenceUseCase){
        return new FingerprintConfirmationPresenter(fingerprintSavePreferenceUseCase, fingerprintGetPreferenceUseCase);
    };

    @FingerprintScope
    @Provides
    PreferenceRepository providePreferenceRepository(DataSourcePreference dataSourcePreference){
        return new PreferenceRepositoryImpl(dataSourcePreference);
    };

    @FingerprintScope
    @Provides
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context){
        return new LocalCacheHandler(context, FINGERPRINT_PREFERENCE);
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
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new TkpdAuthInterceptor())
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        if(GlobalConfig.isAllowDebuggingTools()){
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
