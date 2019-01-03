package com.tokopedia.topchat.common.di;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.di.qualifier.InboxQualifier;
import com.tokopedia.core.network.retrofit.interceptors.DigitalHmacAuthInterceptor;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.topchat.chatlist.data.TopChatUrl;
import com.tokopedia.topchat.chatlist.data.factory.MessageFactory;
import com.tokopedia.topchat.chatlist.data.mapper.DeleteMessageMapper;
import com.tokopedia.topchat.chatlist.data.mapper.GetMessageMapper;
import com.tokopedia.topchat.chatlist.data.repository.MessageRepository;
import com.tokopedia.topchat.chatlist.data.repository.MessageRepositoryImpl;
import com.tokopedia.topchat.chatlist.data.repository.SendMessageSource;
import com.tokopedia.topchat.chatroom.data.mapper.GetExistingChatMapper;
import com.tokopedia.topchat.chatroom.data.mapper.GetReplyMapper;
import com.tokopedia.topchat.chatroom.data.mapper.ReplyMessageMapper;
import com.tokopedia.topchat.chattemplate.data.factory.TemplateChatFactory;
import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper;
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository;
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepositoryImpl;
import com.tokopedia.topchat.common.chat.ChatService;
import com.tokopedia.topchat.uploadimage.data.factory.ImageUploadFactory;
import com.tokopedia.topchat.uploadimage.data.mapper.GenerateHostMapper;
import com.tokopedia.topchat.uploadimage.data.mapper.UploadImageMapper;
import com.tokopedia.topchat.uploadimage.data.repository.ImageUploadRepository;
import com.tokopedia.topchat.uploadimage.data.repository.ImageUploadRepositoryImpl;
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
public class ChatRoomModule {


    @InboxChatScope
    @Provides
    UserSessionInterface provideUserSessionInterface(
            @ApplicationContext Context context) {
        return new UserSession(context);
    }

    @InboxChatScope
    @Provides
    NetworkRouter provideNetworkRouter(
            @ApplicationContext Context context) {
        return (NetworkRouter) context;
    }

    @InboxChatScope
    @Provides
    AnalyticTracker provideAnalyticTracker(
            @ApplicationContext Context context) {
        return ((AbstractionRouter) context).getAnalyticTracker();
    }

    @InboxChatScope
    @Provides
    ChuckInterceptor provideChuckInterceptor(
            @ApplicationContext Context context) {
        return new ChuckInterceptor(context);
    }


    @Provides
    public ErrorResponseInterceptor provideResponseInterceptor() {
        return new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class);
    }

    @InboxChatScope
    @Provides
    OkHttpClient provideOkHttpClient(@InboxQualifier OkHttpRetryPolicy retryPolicy,
                                     ErrorResponseInterceptor errorResponseInterceptor,
                                     ChuckInterceptor chuckInterceptor,
                                     HttpLoggingInterceptor httpLoggingInterceptor,
                                     NetworkRouter networkRouter,
                                     UserSessionInterface userSessionInterface) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new FingerprintInterceptor(networkRouter, userSessionInterface))
                .addInterceptor(new CacheApiInterceptor())
                .addInterceptor(new DigitalHmacAuthInterceptor(AuthUtil.KEY.KEY_WSV4))
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

    @InboxChatScope
    @InboxQualifier
    @Provides
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY);
    }

    @InboxChatScope
    @InboxQualifier
    @Provides
    Retrofit provideChatRetrofit(OkHttpClient okHttpClient,
                                 Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TopChatUrl.BASE_URL)
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }


    @InboxChatScope
    @Provides
    ChatService provideChatService(@InboxQualifier Retrofit retrofit) {
        return new ChatService(retrofit);
    }


    @InboxChatScope
    @Provides
    MessageFactory provideMessageFactory(
            ChatService chatService,
            GetMessageMapper getMessageMapper,
            DeleteMessageMapper deleteMessageMapper) {
        return new MessageFactory(chatService, getMessageMapper, deleteMessageMapper);
    }

//    @InboxChatScope
//    @Provides
//    ReplyFactory provideReplyFactory(
//            ChatService chatService,
//            GetReplyMapper getReplyMapper,
//            ReplyMessageMapper replyMessageMapper,
//            GetExistingChatMapper getExistingChatMapper) {
//        return new ReplyFactory(chatService, getReplyMapper, replyMessageMapper, getExistingChatMapper);
//    }

    @InboxChatScope
    @Provides
    TemplateChatFactory provideTemplateFactory(
            ChatService chatService,
            TemplateChatMapper templateChatMapper) {
        return new TemplateChatFactory(templateChatMapper, chatService);
    }

    @InboxChatScope
    @Provides
    GetReplyMapper provideGetReplyMapper(UserSessionInterface userSession) {
        return new GetReplyMapper(userSession);
    }

    @InboxChatScope
    @Provides
    GetMessageMapper provideGetMessageMapper() {
        return new GetMessageMapper();
    }

    @InboxChatScope
    @Provides
    ReplyMessageMapper provideReplyMessageMapper() {
        return new ReplyMessageMapper();
    }

    @InboxChatScope
    @Provides
    DeleteMessageMapper provideDeleteMessageMapper() {
        return new DeleteMessageMapper();
    }

    @InboxChatScope
    @Provides
    TemplateChatMapper provideTemplateChatMapper() {
        return new TemplateChatMapper();
    }

    @InboxChatScope
    @Provides
    GetExistingChatMapper provideGetExistingMapper() {
        return new GetExistingChatMapper();
    }

    @InboxChatScope
    @Provides
    MessageRepository provideMessageRepository(MessageFactory messageFactory,
                                               SendMessageSource sendMessageSource) {
        return new MessageRepositoryImpl(messageFactory, sendMessageSource);
    }
//
//    @InboxChatScope
//    @Provides
//    ReplyRepository provideReplyRepository(ReplyFactory replyFactory) {
//        return new ReplyRepositoryImpl(replyFactory);
//    }

    @InboxChatScope
    @Provides
    TemplateRepository provideTemplateRepository(TemplateChatFactory templateChatFactory) {
        return new TemplateRepositoryImpl(templateChatFactory);
    }

    @InboxChatScope
    @Provides
    ImageUploadRepository
    provideImageUploadRepository(ImageUploadFactory imageUploadFactory) {
        return new ImageUploadRepositoryImpl(imageUploadFactory);
    }

    @InboxChatScope
    @Provides
    ImageUploadFactory
    provideImageUploadFactory(GenerateHostActService generateHostActService,
                              UploadImageService uploadImageService,
                              GenerateHostMapper generateHostMapper,
                              UploadImageMapper uploadImageMapper) {
        return new ImageUploadFactory(generateHostActService,
                uploadImageService,
                generateHostMapper,
                uploadImageMapper);
    }
}
