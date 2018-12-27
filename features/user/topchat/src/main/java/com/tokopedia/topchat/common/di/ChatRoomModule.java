package com.tokopedia.topchat.common.di;

import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.topchat.chatlist.data.factory.MessageFactory;
import com.tokopedia.topchat.chatlist.data.mapper.DeleteMessageMapper;
import com.tokopedia.topchat.chatlist.data.mapper.GetMessageMapper;
import com.tokopedia.topchat.chatlist.data.repository.MessageRepository;
import com.tokopedia.topchat.chatlist.data.repository.MessageRepositoryImpl;
import com.tokopedia.topchat.chatlist.data.repository.SendMessageSource;
import com.tokopedia.topchat.chatroom.data.factory.ReplyFactory;
import com.tokopedia.topchat.chatroom.data.mapper.GetExistingChatMapper;
import com.tokopedia.topchat.chatroom.data.mapper.GetReplyMapper;
import com.tokopedia.topchat.chatroom.data.mapper.ReplyMessageMapper;
import com.tokopedia.topchat.chatroom.data.repository.ReplyRepository;
import com.tokopedia.topchat.chatroom.data.repository.ReplyRepositoryImpl;
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
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by stevenfredian on 9/14/17.
 */

@Module
public class ChatRoomModule {

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
            ReplyMessageMapper replyMessageMapper,
            GetExistingChatMapper getExistingChatMapper) {
        return new ReplyFactory(chatService, getReplyMapper, replyMessageMapper, getExistingChatMapper);
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

    @InboxChatScope
    @Provides
    ReplyRepository provideReplyRepository(ReplyFactory replyFactory) {
        return new ReplyRepositoryImpl(replyFactory);
    }

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
