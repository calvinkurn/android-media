package com.tokopedia.tkpdreactnative.react.fingerprint.di;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.AccountFingerprintApi;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.FingerprintApi;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.FingerprintDataSourceCloud;
import com.tokopedia.tkpdreactnative.react.fingerprint.data.FingerprintRepositoryImpl;
import com.tokopedia.tkpdreactnative.react.fingerprint.domain.FingerprintRepository;
import com.tokopedia.tkpdreactnative.react.fingerprint.domain.SaveFingerPrintUseCase;
import com.tokopedia.tkpdreactnative.react.fingerprint.utils.FingerprintConstant;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.presenter.SaveFingerPrintPresenter;

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

    @FingerprintScope
    @Provides
    SaveFingerPrintPresenter provideSaveFingerPrintPresenter(SaveFingerPrintUseCase saveFingerPrintUseCase,
                                                    UserSession userSession){
        return new SaveFingerPrintPresenter(userSession, saveFingerPrintUseCase);
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
        return new OkHttpClient.Builder()
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }
}
