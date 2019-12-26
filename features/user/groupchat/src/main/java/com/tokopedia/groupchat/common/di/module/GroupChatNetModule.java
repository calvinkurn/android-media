package com.tokopedia.groupchat.common.di.module;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerCollector;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.chuckerteam.chucker.api.RetentionManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.groupchat.common.di.scope.GroupChatScope;
import com.tokopedia.groupchat.common.network.PlayInterceptor;
import com.tokopedia.groupchat.common.network.StreamErrorInterceptor;
import com.tokopedia.groupchat.common.network.StreamErrorResponse;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

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
    public NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter) context;
    }

    @GroupChatScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @GroupChatScope
    @Provides
    public StreamErrorInterceptor provideStreamErrorInterceptor() {
        return new StreamErrorInterceptor(StreamErrorResponse.class);
    }

    @GroupChatScope
    @Provides
    public ChuckerInterceptor provideChuckerInterceptor(@ApplicationContext Context context) {
        ChuckerCollector collector = new ChuckerCollector(
                context, GlobalConfig.isAllowDebuggingTools(), RetentionManager.Period.ONE_HOUR);

        return new ChuckerInterceptor(
                context, collector, 120000L);
    }

    @GroupChatScope
    @Provides
    public TkpdAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                          NetworkRouter networkRouter,
                                                          UserSessionInterface userSessionInterface) {
        return new TkpdAuthInterceptor(context, networkRouter, userSessionInterface);
    }

    @GroupChatScope
    @Provides
    public OkHttpClient provideOkHttpClient(ChuckerInterceptor chuckInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor,
                                            PlayInterceptor playInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new ErrorResponseInterceptor(StreamErrorResponse.class))
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(playInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }


}
