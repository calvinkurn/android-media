package com.tokopedia.topchat.common.di;


import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.di.qualifier.InboxQualifier;
import com.tokopedia.core.network.retrofit.interceptors.DigitalHmacAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topchat.chatlist.data.factory.MessageFactory;
import com.tokopedia.topchat.chatlist.data.factory.SearchFactory;
import com.tokopedia.topchat.chatlist.data.mapper.DeleteMessageMapper;
import com.tokopedia.topchat.chatlist.data.mapper.GetMessageMapper;
import com.tokopedia.topchat.chatlist.data.mapper.SearchChatMapper;
import com.tokopedia.topchat.chatlist.data.repository.MessageRepository;
import com.tokopedia.topchat.chatlist.data.repository.MessageRepositoryImpl;
import com.tokopedia.topchat.chatlist.data.repository.SearchRepository;
import com.tokopedia.topchat.chatlist.data.repository.SearchRepositoryImpl;
import com.tokopedia.topchat.chatlist.data.repository.SendMessageSource;
import com.tokopedia.topchat.chatlist.domain.usecase.DeleteMessageListUseCase;
import com.tokopedia.topchat.chatlist.domain.usecase.GetMessageListUseCase;
import com.tokopedia.topchat.chatlist.domain.usecase.SearchMessageUseCase;
import com.tokopedia.topchat.chatroom.data.factory.ReplyFactory;
import com.tokopedia.topchat.chatroom.data.mapper.GetReplyMapper;
import com.tokopedia.topchat.chatroom.data.mapper.ReplyMessageMapper;
import com.tokopedia.topchat.chatroom.data.mapper.SendMessageMapper;
import com.tokopedia.topchat.chatroom.data.network.ChatBotApi;
import com.tokopedia.topchat.chatroom.data.network.ChatBotUrl;
import com.tokopedia.topchat.chatroom.data.repository.ReplyRepository;
import com.tokopedia.topchat.chatroom.data.repository.ReplyRepositoryImpl;
import com.tokopedia.topchat.chatroom.domain.AttachImageUseCase;
import com.tokopedia.topchat.chatroom.domain.GetReplyListUseCase;
import com.tokopedia.topchat.chatroom.domain.ReplyMessageUseCase;
import com.tokopedia.topchat.chatroom.domain.SendMessageUseCase;
import com.tokopedia.topchat.chattemplate.data.factory.TemplateChatFactory;
import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper;
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository;
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepositoryImpl;
import com.tokopedia.topchat.chattemplate.domain.usecase.GetTemplateUseCase;
import com.tokopedia.topchat.uploadimage.data.factory.ImageUploadFactory;
import com.tokopedia.topchat.uploadimage.data.mapper.GenerateHostMapper;
import com.tokopedia.topchat.uploadimage.data.mapper.UploadImageMapper;
import com.tokopedia.topchat.uploadimage.data.repository.ImageUploadRepository;
import com.tokopedia.topchat.uploadimage.data.repository.ImageUploadRepositoryImpl;
import com.tokopedia.topchat.uploadimage.domain.interactor.GenerateHostUseCase;
import com.tokopedia.topchat.uploadimage.domain.interactor.UploadImageUseCase;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by stevenfredian on 9/14/17.
 */

@Module
public class InboxChatModule {

    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    @InboxChatScope
    @Provides
    MessageFactory provideMessageFactory(
            ChatService chatService,
            GetMessageMapper getMessageMapper,
            DeleteMessageMapper deleteMessageMapper) {
        return new MessageFactory(chatService, getMessageMapper, deleteMessageMapper);
    }

    @InboxChatScope
    @Provides
    ReplyFactory provideReplyFactory(
            ChatService chatService,
            GetReplyMapper getReplyMapper,
            ReplyMessageMapper replyMessageMapper) {
        return new ReplyFactory(chatService, getReplyMapper, replyMessageMapper);
    }

    @InboxChatScope
    @Provides
    SearchFactory provideSearchFactory(
            ChatService chatService,
            SearchChatMapper searchChatMapper) {
        return new SearchFactory(chatService, searchChatMapper);
    }


    @InboxChatScope
    @Provides
    TemplateChatFactory provideTemplateFactory(
            ChatService chatService,
            TemplateChatMapper templateChatMapper) {
        return new TemplateChatFactory(templateChatMapper, chatService);
    }


    @InboxChatScope
    @Provides
    GetReplyMapper provideGetReplyMapper(SessionHandler sessionHandler) {
        return new GetReplyMapper(sessionHandler);
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
    SearchChatMapper provideSearchChatMapper() {
        return new SearchChatMapper();
    }


    @InboxChatScope
    @Provides
    TemplateChatMapper provideTemplateChatMapper() {
        return new TemplateChatMapper();
    }


    @InboxChatScope
    @Provides
    MessageRepository provideMessageRepository(MessageFactory messageFactory,
                                               SendMessageSource sendMessageSource) {
        return new MessageRepositoryImpl(messageFactory, sendMessageSource);
    }

    @InboxChatScope
    @Provides
    ReplyRepository provideReplyRepository(ReplyFactory replyFactory) {
        return new ReplyRepositoryImpl(replyFactory);
    }

    @InboxChatScope
    @Provides
    SearchRepository provideSearchRepository(SearchFactory searchFactory) {
        return new SearchRepositoryImpl(searchFactory);
    }


    @InboxChatScope
    @Provides
    TemplateRepository provideTemplateRepository(TemplateChatFactory templateChatFactory) {
        return new TemplateRepositoryImpl(templateChatFactory);
    }

    @InboxChatScope
    @Provides
    GetMessageListUseCase provideGetMessageListUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutor,
                                                       MessageRepository messageRepository) {
        return new GetMessageListUseCase(threadExecutor, postExecutor, messageRepository);
    }


    @InboxChatScope
    @Provides
    GetReplyListUseCase provideGetReplyListUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutor,
                                                   ReplyRepository replyRepository) {
        return new GetReplyListUseCase(threadExecutor, postExecutor, replyRepository);
    }


    @InboxChatScope
    @Provides
    ReplyMessageUseCase provideReplyMessageUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutor,
                                                   ReplyRepository replyRepository) {
        return new ReplyMessageUseCase(threadExecutor, postExecutor, replyRepository);
    }

    @InboxChatScope
    @Provides
    SearchMessageUseCase provideSearchChatUseCase(ThreadExecutor threadExecutor,
                                                  PostExecutionThread postExecutor,
                                                  SearchRepository searchRepository) {
        return new SearchMessageUseCase(threadExecutor, postExecutor, searchRepository);
    }

    @InboxChatScope
    @Provides
    DeleteMessageListUseCase provideDeleteChatUseCase(ThreadExecutor threadExecutor,
                                                      PostExecutionThread postExecutor,
                                                      MessageRepository messageRepository) {
        return new DeleteMessageListUseCase(threadExecutor, postExecutor, messageRepository);
    }

    @InboxChatScope
    @Provides
    GetTemplateUseCase provideGetTemplateUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutor,
                                                 TemplateRepository templateRepository) {
        return new GetTemplateUseCase(threadExecutor, postExecutor, templateRepository);
    }


    @InboxChatScope
    @Provides
    ChatService provideChatService() {
        return new ChatService();
    }

    @InboxChatScope
    @Provides
    KunyitService provideKunyitService() {
        return new KunyitService();
    }

    @InboxChatScope
    @Provides
    SendMessageMapper provideSendMessageMapper() {
        return new SendMessageMapper();
    }


    @InboxChatScope
    @Provides
    SendMessageSource provideSendMessageSource(ChatService chatService,
                                               SendMessageMapper sendMessageMapper) {
        return new SendMessageSource(chatService, sendMessageMapper);
    }

    @InboxChatScope
    @Provides
    SendMessageUseCase provideSendMessageUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutor,
                                                 MessageRepository messageRepository) {
        return new SendMessageUseCase(
                threadExecutor,
                postExecutor,
                messageRepository
        );
    }

    @InboxChatScope
    @Provides
    AttachImageUseCase provideAttachImageUsecase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutor,
                                                 GenerateHostUseCase generateHostUseCase,
                                                 UploadImageUseCase uploadImageUseCase,
                                                 ReplyMessageUseCase replyMessageUseCase) {
        return new AttachImageUseCase(threadExecutor, postExecutor, generateHostUseCase, uploadImageUseCase, replyMessageUseCase);
    }

    @InboxChatScope
    @Provides
    UploadImageUseCase
    provideUploadImageUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              ImageUploadRepository imageUploadRepository) {
        return new UploadImageUseCase(
                threadExecutor,
                postExecutionThread,
                imageUploadRepository);
    }

    @InboxChatScope
    @Provides
    GenerateHostUseCase
    provideGenerateHostUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               ImageUploadRepository imageUploadRepository) {
        return new GenerateHostUseCase(
                threadExecutor,
                postExecutionThread,
                imageUploadRepository);
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

    @InboxChatScope
    @Provides
    GenerateHostActService
    provideGenerateHostActService() {
        return new GenerateHostActService();
    }

    @InboxChatScope
    @Provides
    UploadImageService
    provideUploadImageService() {
        return new UploadImageService();
    }

    @InboxChatScope
    @Provides
    OkHttpClient provideOkHttpClient(@InboxQualifier OkHttpRetryPolicy retryPolicy){
        return new OkHttpClient.Builder()
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new CacheApiInterceptor())
                .addInterceptor(new DigitalHmacAuthInterceptor(AuthUtil.KEY.KEY_WSV4))
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .build();
    }

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
                                 Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(ChatBotUrl.BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @InboxChatScope
    @Provides
    ChatBotApi provideChatRatingApi(@InboxQualifier Retrofit retrofit){
        return retrofit.create(ChatBotApi.class);
    }
}
