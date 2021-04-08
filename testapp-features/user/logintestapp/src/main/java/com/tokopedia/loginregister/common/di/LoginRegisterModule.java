package com.tokopedia.loginregister.common.di;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.loginregister.common.data.LoginRegisterUrl;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.utils.permission.PermissionCheckerHelper;
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import static com.tokopedia.sessioncommon.di.SessionModule.getUserAgent;

/**
 * @author by nisie on 10/15/18.
 */
@Module
public class LoginRegisterModule {

    @LoginRegisterScope
    @Provides
    OkHttpClient provideOkHttpClient(@ApplicationContext Context context,
                                     TkpdOldAuthInterceptor tkpdAuthInterceptor,
                                     ChuckerInterceptor chuckInterceptor,
                                     DebugInterceptor debugInterceptor,
                                     HttpLoggingInterceptor httpLoggingInterceptor,
                                     FingerprintInterceptor fingerprintInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(tkpdAuthInterceptor);
        builder.addInterceptor(fingerprintInterceptor);
        builder.addInterceptor(chain -> {
            Request.Builder newRequest = chain.request().newBuilder();
            newRequest.addHeader("User-Agent", getUserAgent());
            return chain.proceed(newRequest.build());
        });
        builder.addInterceptor(new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class));
        builder.addInterceptor(new ErrorResponseInterceptor(TkpdV4ResponseError.class));
        builder.addInterceptor(new AkamaiBotInterceptor(context));

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor);
            builder.addInterceptor(debugInterceptor);
            builder.addInterceptor(httpLoggingInterceptor);
        }

        return builder.build();
    }

    @LoginRegisterScope
    @Provides
    Retrofit provideLoginRegisterRetrofit(Retrofit.Builder retrofitBuilder,
                                          @LoginRegisterScope OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(LoginRegisterUrl.BASE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @LoginRegisterScope
    @Provides
    PermissionCheckerHelper providePermissionCheckerHelper(){
        return new PermissionCheckerHelper();
    }
}
