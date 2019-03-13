package com.tokopedia.otp.common.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.otp.common.network.AccountsAuthorizationInterceptor;
import com.tokopedia.otp.common.network.AuthorizationBearerInterceptor;
import com.tokopedia.otp.common.network.OtpErrorInterceptor;
import com.tokopedia.otp.common.network.OtpErrorResponse;
import com.tokopedia.otp.common.network.WSErrorResponse;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author by nisie on 4/25/18.
 */

@OtpScope
@Module
public class OtpNetModule {

    @OtpScope
    @Provides
    public OtpErrorInterceptor provideStreamErrorInterceptor() {
        return new OtpErrorInterceptor(OtpErrorResponse.class);
    }

    @OtpScope
    @Provides
    public AccountsAuthorizationInterceptor provideAccountsAuthorizationInterceptor(UserSessionInterface
                                                                                            userSession) {
        return new AccountsAuthorizationInterceptor(userSession);
    }

    @OtpScope
    @Provides
    public ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools());
    }

    @OtpScope
    @Provides
    public FingerprintInterceptor provideFingerprintInterceptor(@ApplicationContext Context context,
                                                                UserSessionInterface userSession) {
        return new FingerprintInterceptor((NetworkRouter) context, userSession);
    }

    @OtpScope
    @Provides
    public AuthorizationBearerInterceptor provideAuthorizationBearerInterceptor(UserSessionInterface
                                                                                        userSession) {
        return new AuthorizationBearerInterceptor(userSession);
    }

    @OtpScope
    @Provides
    public OkHttpClient provideOkHttpClient(FingerprintInterceptor fingerprintInterceptor,
                                            AuthorizationBearerInterceptor
                                                    authorizationBearerInterceptor,
                                            ChuckInterceptor chuckInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(new ErrorResponseInterceptor(OtpErrorResponse.class))
                .addInterceptor(new ErrorResponseInterceptor(WSErrorResponse.class))
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(authorizationBearerInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor).addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }

    @OtpScope
    @Provides
    @MethodListQualifier
    public OkHttpClient provideMethodListOkHttpClient(FingerprintInterceptor fingerprintInterceptor,
                                                      ChuckInterceptor chuckInterceptor,
                                                      HttpLoggingInterceptor httpLoggingInterceptor,
                                                      TkpdAuthInterceptor tkpdAuthInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(new ErrorResponseInterceptor(OtpErrorResponse.class))
                .addInterceptor(tkpdAuthInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor).addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }


}
