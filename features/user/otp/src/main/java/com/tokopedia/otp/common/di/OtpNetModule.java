package com.tokopedia.otp.common.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.otp.common.network.AccountsAuthorizationInterceptor;
import com.tokopedia.otp.common.network.OtpErrorInterceptor;
import com.tokopedia.otp.common.network.OtpErrorResponse;

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
    public AccountsAuthorizationInterceptor provideAccountsAuthorizationInterceptor(UserSession
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
    public OkHttpClient provideOkHttpClient(ChuckInterceptor chuckInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor,
                                            AccountsAuthorizationInterceptor accountsAuthorizationInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(accountsAuthorizationInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor).addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }
}
