package com.tokopedia.topchat.chattemplate.di;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.topchat.chatlist.data.TopChatUrl;
import com.tokopedia.topchat.chattemplate.data.factory.EditTemplateChatFactory;
import com.tokopedia.topchat.chattemplate.data.factory.TemplateChatFactory;
import com.tokopedia.topchat.chattemplate.data.mapper.EditTemplateChatMapper;
import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper;
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepository;
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepositoryImpl;
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository;
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepositoryImpl;
import com.tokopedia.topchat.common.chat.api.ChatApi;
import com.tokopedia.topchat.common.di.qualifier.InboxQualifier;
import com.tokopedia.topchat.common.network.XUserIdInterceptor;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by stevenfredian on 9/14/17.
 */

@Module
public class TemplateChatModule {

    @TemplateChatScope
    @Provides
    UserSessionInterface provideUserSessionInterface(
            @ApplicationContext Context context) {
        return new UserSession(context);
    }

    @TemplateChatScope
    @Provides
    UserSession provideUserSession(
            @ApplicationContext Context context) {
        return new UserSession(context);
    }

    @TemplateChatScope
    @Provides
    NetworkRouter provideNetworkRouter(
            @ApplicationContext Context context) {
        return (NetworkRouter) context;
    }

    @TemplateChatScope
    @Provides
    ChuckInterceptor provideChuckInterceptor(
            @ApplicationContext Context context) {
        return new ChuckInterceptor(context);
    }


    @Provides
    public ErrorResponseInterceptor provideResponseInterceptor() {
        return new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class);
    }

    @Provides
    public XUserIdInterceptor provideXUserIdInterceptor(@ApplicationContext Context context,
                                                        NetworkRouter networkRouter,
                                                        UserSession userSession) {
        return new XUserIdInterceptor(context, networkRouter, userSession);
    }

    @Provides
    public TkpdAuthInterceptor provideChatTkpdAuthInterceptor(@ApplicationContext Context context,
                                                              NetworkRouter networkRouter,
                                                              UserSessionInterface userSessionInterface) {
        return new TkpdAuthInterceptor(context, networkRouter, userSessionInterface);
    }


    @TemplateChatScope
    @Provides
    OkHttpClient provideOkHttpClient(@InboxQualifier OkHttpRetryPolicy retryPolicy,
                                     ErrorResponseInterceptor errorResponseInterceptor,
                                     ChuckInterceptor chuckInterceptor,
                                     HttpLoggingInterceptor httpLoggingInterceptor,
                                     NetworkRouter networkRouter,
                                     UserSessionInterface userSessionInterface,
                                     XUserIdInterceptor xUserIdInterceptor,
                                     TkpdAuthInterceptor tkpdAuthInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new FingerprintInterceptor(networkRouter, userSessionInterface))
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(new CacheApiInterceptor())
                .addInterceptor(errorResponseInterceptor)
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }

    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    @TemplateChatScope
    @InboxQualifier
    @Provides
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY);
    }

    @TemplateChatScope
    @InboxQualifier
    @Provides
    Retrofit provideChatRetrofit(OkHttpClient okHttpClient,
                                 Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TopChatUrl.Companion.getBASE_URL())
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @TemplateChatScope
    @Provides
    ChatApi provideChatApi(@InboxQualifier Retrofit retrofit) {
        return retrofit.create(ChatApi.class);
    }

    @TemplateChatScope
    @Provides
    TemplateChatFactory provideTemplateChatFactory(
            ChatApi chatApi,
            TemplateChatMapper templateChatMapper) {
        return new TemplateChatFactory(templateChatMapper, chatApi);
    }

    @TemplateChatScope
    @Provides
    EditTemplateChatFactory provideEditTemplateChatFactory(
            ChatApi chatApi,
            EditTemplateChatMapper templateChatMapper) {
        return new EditTemplateChatFactory(templateChatMapper, chatApi);
    }

    @TemplateChatScope
    @Provides
    TemplateRepository provideTemplateRepository(TemplateChatFactory templateChatFactory) {
        return new TemplateRepositoryImpl(templateChatFactory);
    }

    @TemplateChatScope
    @Provides
    EditTemplateRepository provideEditTemplateRepository(EditTemplateChatFactory templateChatFactory) {
        return new EditTemplateRepositoryImpl(templateChatFactory);
    }

}
