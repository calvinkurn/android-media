package com.tokopedia.changephonenumber.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.changephonenumber.ChangePhoneNumberRouter;
import com.tokopedia.changephonenumber.ChangePhoneNumberUrl;
import com.tokopedia.changephonenumber.analytics.ChangePhoneNumberAnalytics;
import com.tokopedia.changephonenumber.data.api.ChangePhoneNumberApi;
import com.tokopedia.changephonenumber.data.interceptor.ChangePhoneNumberInterceptor;
import com.tokopedia.changephonenumber.data.repository.ChangePhoneNumberRepositoryImpl;
import com.tokopedia.changephonenumber.data.source.CloudGetWarningSource;
import com.tokopedia.changephonenumber.data.source.CloudValidateNumberSource;
import com.tokopedia.changephonenumber.data.source.CloudValidateOtpStatus;
import com.tokopedia.changephonenumber.domain.ChangePhoneNumberRepository;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author by alvinatin on 25/09/18.
 */

@ChangePhoneNumberScope
@Module
public class ChangePhoneNumberModule {

    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    @Provides
    @ChangePhoneNumberQualifier
    OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor
                                             httpLoggingInterceptor,
                                     @ChangePhoneNumberQualifier TkpdAuthInterceptor
                                             tkpdAuthInterceptor,
                                     @ChangePhoneNumberQualifier ChangePhoneNumberInterceptor
                                             changePhoneNumberInterceptor,
                                     @ChangePhoneNumberQualifier OkHttpRetryPolicy retryPolicy,
                                     @ChangePhoneNumberQualifier FingerprintInterceptor
                                             fingerprintInterceptor,
                                     @ChangePhoneNumberQualifier ErrorResponseInterceptor
                                             errorResponseInterceptor,
                                     @ChangePhoneNumberChuckQualifier Interceptor chuckInterceptor) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(changePhoneNumberInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(errorResponseInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor);
            clientBuilder.addInterceptor(chuckInterceptor);
        }

        return clientBuilder.build();
    }

    @Provides
    @ChangePhoneNumberQualifier
    Retrofit provideRetrofit(@ChangePhoneNumberQualifier OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(ChangePhoneNumberUrl.BASE_URL)
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    ChangePhoneNumberApi provideChangePhoneNumberApi(@ChangePhoneNumberQualifier Retrofit
                                                             retrofit) {
        return retrofit.create(ChangePhoneNumberApi.class);
    }

    @Provides
    @ChangePhoneNumberQualifier
    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY);
    }

    @Provides
    @ChangePhoneNumberQualifier
    public FingerprintInterceptor provideFingerPrintInterceptor(@ApplicationContext Context context,
                                                                @ChangePhoneNumberQualifier
                                                                        UserSession userSession) {
        return new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(),
                userSession);
    }

    @Provides
    @ChangePhoneNumberQualifier
    public TkpdAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                          @ChangePhoneNumberQualifier NetworkRouter networkRouter,
                                                          @ChangePhoneNumberQualifier UserSession userSession) {
        return new TkpdAuthInterceptor(context, networkRouter, userSession);
    }

    @Provides
    @ChangePhoneNumberQualifier
    ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(TkpdV4ResponseError.class);
    }

    @Provides
    ChangePhoneNumberAnalytics provideChangePhoneNumberAnalytics(@ApplicationContext Context
                                                                         context) {
        return new ChangePhoneNumberAnalytics(context);
    }

    @Provides
    @ChangePhoneNumberQualifier
    public UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    ChangePhoneNumberRepository provideChangePhoneNumberRepository(CloudGetWarningSource
                                                                           cloudGetWarningSource,
                                                                   CloudValidateNumberSource
                                                                           cloudValidateNumberSource,
                                                                   CloudValidateOtpStatus
                                                                           cloudValidateOtpStatus) {
        return new ChangePhoneNumberRepositoryImpl(cloudGetWarningSource,
                cloudValidateNumberSource, cloudValidateOtpStatus);
    }

    @Provides
    @ChangePhoneNumberQualifier
    public NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        if (context instanceof NetworkRouter) {
            return ((NetworkRouter) context);
        } else {
            return null;
        }
    }

    @Provides
    @ChangePhoneNumberQualifier
    public ChangePhoneNumberInterceptor provideChangePhoneNumberInterceptor(@ChangePhoneNumberQualifier UserSession
                                                                      userSession) {
        return new ChangePhoneNumberInterceptor(userSession);
    }

    @Provides
    @ChangePhoneNumberChuckQualifier
    public Interceptor provideChuckInterceptory(@ApplicationContext Context context) {
        if (context instanceof ChangePhoneNumberRouter) {
            return ((ChangePhoneNumberRouter) context).getChuckInterceptor();
        }
        throw new RuntimeException("App should implement " + ChangePhoneNumberRouter.class
 .getSimpleName());
    }
}
