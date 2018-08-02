package com.tokopedia.withdraw.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.AccountsAuthorizationInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author by StevenFredian on 30/07/18.
 */

@WithdrawScope
@Module
public class WithdrawModule {

    @WithdrawQualifier
    @Provides
    public ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools());
    }

    @WithdrawQualifier
    @Provides
    public AccountsAuthorizationInterceptor provideAccountsAuthorizationInterceptor(com.tokopedia.abstraction.common.data.model.session.UserSession userSession) {
        return new AccountsAuthorizationInterceptor(userSession);
    }

    @WithdrawQualifier
    @Provides
    public UserSession provideUserSession(@ApplicationContext Context context){
        return new UserSession(context);
    }

    @WithdrawQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(
            @ApplicationContext Context context,
            @WithdrawQualifier ChuckInterceptor chuckInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            @WithdrawQualifier AccountsAuthorizationInterceptor accountsAuthorizationInterceptor,
                                            @WithdrawQualifier UserSession userSession) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(accountsAuthorizationInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new TkpdAuthInterceptor(context, (NetworkRouter) context.getApplicationContext(), userSession))
                .addInterceptor(new FingerprintInterceptor((NetworkRouter) context, userSession));

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(new DebugInterceptor());
        }
        return builder.build();
    }

}
