package com.tokopedia.forgotpassword.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.forgotpassword.analytics.ForgotPasswordAnalytics;
import com.tokopedia.forgotpassword.data.ForgotPasswordApi;
import com.tokopedia.forgotpassword.data.ForgotPasswordUrl;
import com.tokopedia.forgotpassword.network.OldTkpdAuthInterceptor;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by nisie on 9/25/18.
 */
@Module
public class ForgotPasswordModule {

    @ForgotPasswordScope
    @Provides
    ForgotPasswordAnalytics provideForgotPasswordAnalytics(@ApplicationContext Context context) {
        return new ForgotPasswordAnalytics(((AbstractionRouter) context).getAnalyticTracker());
    }


    @ForgotPasswordScope
    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ForgotPasswordScope
    @Provides
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ForgotPasswordScope
    @Provides
    ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context);
    }

    @ForgotPasswordScope
    @Provides
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter) context;
    }


    @ForgotPasswordScope
    @Provides
    TkpdAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                   NetworkRouter networkRouter,
                                                   UserSession userSession) {
        return new TkpdAuthInterceptor(context, networkRouter, userSession);
    }

    @ForgotPasswordScope
    @Provides
    public ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(TkpdV4ResponseError.class);
    }

    @ForgotPasswordScope
    @Provides
    FingerprintInterceptor provideFingerprintInterceptor(NetworkRouter networkRouter,
                                                         UserSession userSession) {
        return new FingerprintInterceptor(networkRouter, userSession);
    }

    @ForgotPasswordScope
    @Provides
    OldTkpdAuthInterceptor provideOldTkpdAuthInterceptor(@ApplicationContext Context context,
                                                         NetworkRouter networkRouter,
                                                         UserSession userSession) {
        return new OldTkpdAuthInterceptor(context, networkRouter, userSession);
    }

    @ForgotPasswordScope
    @Provides
    public OkHttpClient provideOkHttpClient(ChuckInterceptor chuckInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor,
                                            FingerprintInterceptor fingerprintInterceptor,
                                            ErrorResponseInterceptor errorResponseInterceptor,
                                            OldTkpdAuthInterceptor oldTkpdAuthInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(oldTkpdAuthInterceptor)
                .addInterceptor(errorResponseInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }

    @ForgotPasswordScope
    @Provides
    Retrofit provideForgotPasswordRetrofit(Retrofit.Builder retrofitBuilder,
                                           @ForgotPasswordScope OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(ForgotPasswordUrl.BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @ForgotPasswordScope
    @Provides
    ForgotPasswordApi provideForgotPasswordApi(@ForgotPasswordScope Retrofit retrofit) {
        return retrofit.create(ForgotPasswordApi.class);
    }


}
