package com.tokopedia.groupchat.common.di.module;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.AccountsAuthorizationInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.groupchat.common.di.scope.GroupChatScope;
import com.tokopedia.groupchat.common.network.StreamErrorInterceptor;
import com.tokopedia.groupchat.common.network.StreamErrorResponse;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author by nisie on 2/1/18.
 */

@GroupChatScope
@Module
public class GroupChatNetModule {

    @GroupChatScope
    @Provides
    public StreamErrorInterceptor provideStreamErrorInterceptor() {
        return new StreamErrorInterceptor(StreamErrorResponse.class);
    }

    @GroupChatScope
    @Provides
    public AccountsAuthorizationInterceptor provideAccountsAuthorizationInterceptor(@ApplicationContext Context context) {
        return new AccountsAuthorizationInterceptor(context);
    }

    @GroupChatScope
    @Provides
    public ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools());
    }


    @GroupChatScope
    @Provides
    public OkHttpClient provideOkHttpClient(ChuckInterceptor chuckInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor,
                                            AccountsAuthorizationInterceptor accountsAuthorizationInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new ErrorResponseInterceptor(StreamErrorResponse.class))
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(accountsAuthorizationInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }


}
