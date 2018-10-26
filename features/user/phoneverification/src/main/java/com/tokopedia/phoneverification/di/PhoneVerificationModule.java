package com.tokopedia.phoneverification.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.phoneverification.data.network.PhoneVerificationBearerInterceptor;
import com.tokopedia.phoneverification.PhoneVerificationConst;
import com.tokopedia.phoneverification.data.network.PhoneVerificationInterceptor;
import com.tokopedia.phoneverification.PhoneVerificationRouter;
import com.tokopedia.phoneverification.data.PhoneVerificationApi;
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
 * @author by alvinatin on 12/10/18.
 */

@PhoneVerificationScope
@Module
public class PhoneVerificationModule {
    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    @Provides
    @PhoneVerificationQualifier
    OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor
                                             httpLoggingInterceptor,
                                     @PhoneVerificationQualifier PhoneVerificationInterceptor
                                             phoneVerificationInterceptor,
                                     @PhoneVerificationQualifier PhoneVerificationBearerInterceptor
                                             phoneVerificationBearerInterceptor,
                                     @PhoneVerificationQualifier OkHttpRetryPolicy retryPolicy,
                                     @PhoneVerificationQualifier FingerprintInterceptor
                                             fingerprintInterceptor,
                                     @PhoneVerificationQualifier ErrorResponseInterceptor
                                             errorResponseInterceptor,
                                     @PhoneVerificationChuckQualifier Interceptor chuckInterceptor) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(phoneVerificationInterceptor)
                .addInterceptor(phoneVerificationBearerInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(errorResponseInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor);
            clientBuilder.addInterceptor(chuckInterceptor);
        }

        return clientBuilder.build();
    }

    @Provides
    @PhoneVerificationQualifier
    Retrofit provideRetrofit(@PhoneVerificationQualifier OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(PhoneVerificationConst.BASE_URL)
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @PhoneVerificationQualifier
    PhoneVerificationInterceptor providePhoneVerificationInterceptor(@ApplicationContext Context context,
                                                   @PhoneVerificationQualifier NetworkRouter networkRouter,
                                                   UserSession userSession){
        return new PhoneVerificationInterceptor(context, networkRouter, userSession);
    }

    @Provides
    @PhoneVerificationQualifier
    PhoneVerificationBearerInterceptor providePhoneVerificationBearerInterceptor(UserSession userSession){
        return new PhoneVerificationBearerInterceptor(userSession);
    }

    @Provides
    PhoneVerificationApi provideChangePhoneNumberApi(@PhoneVerificationQualifier Retrofit retrofit) {
        return retrofit.create(PhoneVerificationApi.class);
    }

    @Provides
    @PhoneVerificationQualifier
    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY);
    }

    @Provides
    @PhoneVerificationQualifier
    public FingerprintInterceptor provideFingerPrintInterceptor(@ApplicationContext Context context,
                                                                UserSession userSession) {
        return new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(),
                userSession);
    }

    @Provides
    @PhoneVerificationQualifier
    ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(TkpdV4ResponseError.class);
    }

    @Provides
    public UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    @PhoneVerificationQualifier
    public NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        if (context instanceof NetworkRouter) {
            return ((NetworkRouter) context);
        } else {
            return null;
        }
    }

    @Provides
    @PhoneVerificationChuckQualifier
    public Interceptor provideChuckInterceptor(@ApplicationContext Context context) {
        if (context instanceof PhoneVerificationRouter) {
            return ((PhoneVerificationRouter) context).getChuckInterceptor();
        }
        throw new RuntimeException("App should implement " + PhoneVerificationRouter.class
                .getSimpleName());
    }
}
