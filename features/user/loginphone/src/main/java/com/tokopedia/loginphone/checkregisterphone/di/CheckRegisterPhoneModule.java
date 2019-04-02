package com.tokopedia.loginphone.checkregisterphone.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.loginphone.checkregisterphone.data.CheckMsisdnApi;
import com.tokopedia.loginphone.checkregisterphone.data.CheckMsisdnUrl;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import static com.tokopedia.sessioncommon.di.SessionModule.SESSION_MODULE;

/**
 * @author by nisie on 10/22/18.
 */
@Module
public class CheckRegisterPhoneModule {


    @CheckRegisterPhoneScope
    @Provides
    TkpdOldAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                      NetworkRouter networkRouter,
                                                      @Named(SESSION_MODULE) UserSessionInterface userSession) {
        return new TkpdOldAuthInterceptor(context, networkRouter, userSession);
    }

    @CheckRegisterPhoneScope
    @Provides
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter) context;
    }

    @CheckRegisterPhoneScope
    @Provides
    ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context);
    }

    @CheckRegisterPhoneScope
    @Provides
    FingerprintInterceptor provideFingerprintInterceptor(@Named(SESSION_MODULE) UserSessionInterface userSessionInterface,
                                                         NetworkRouter networkRouter) {
        return new FingerprintInterceptor(networkRouter, userSessionInterface);
    }

    @CheckRegisterPhoneScope
    @Provides
    OkHttpClient provideOkHttpClient(TkpdOldAuthInterceptor tkpdAuthInterceptor,
                                     ChuckInterceptor chuckInterceptor,
                                     DebugInterceptor debugInterceptor,
                                     HttpLoggingInterceptor httpLoggingInterceptor,
                                     FingerprintInterceptor fingerprintInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(fingerprintInterceptor);
        builder.addInterceptor(tkpdAuthInterceptor);
        builder.addInterceptor(new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class));
        builder.addInterceptor(new ErrorResponseInterceptor(TkpdV4ResponseError.class));

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor);
            builder.addInterceptor(debugInterceptor);
            builder.addInterceptor(httpLoggingInterceptor);
        }

        return builder.build();
    }

    @CheckRegisterPhoneScope
    @Provides
    Retrofit provideLoginRegisterPhoneRetrofit(Retrofit.Builder retrofitBuilder,
                                               @CheckRegisterPhoneScope OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(CheckMsisdnUrl.BASE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @CheckRegisterPhoneScope
    @Provides
    CheckMsisdnApi provideCheckMsisdnApi(@CheckRegisterPhoneScope Retrofit retrofit) {
        return retrofit.create(CheckMsisdnApi.class);
    }

}
