package com.tokopedia.topchat.stub.chatroom.di

import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.domain.usecase.ChatAttachmentUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import com.tokopedia.topchat.stub.chatroom.usecase.ChatAttachmentUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import dagger.Module
import dagger.Provides

@Module
class ChatRoomFakeUseCaseModule {

    @Provides
    @ChatScope
    fun provideGetChatUseCase(
            getChatUseCaseStub: GetChatUseCaseStub
    ): GetChatUseCase = getChatUseCaseStub

    @Provides
    @ChatScope
    fun provideChatAttachmentUseCase(
            chatAttachmentUseCaseStub: ChatAttachmentUseCaseStub
    ): ChatAttachmentUseCase = chatAttachmentUseCaseStub

    @Provides
    @ChatScope
    fun provideGetChatUseCaseStub(): GetChatUseCaseStub = GetChatUseCaseStub()

    @Provides
    @ChatScope
    fun provideChatAttachmentUseCaseStub(): ChatAttachmentUseCaseStub = ChatAttachmentUseCaseStub()
}