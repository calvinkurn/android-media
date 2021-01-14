package com.tokopedia.topchat.stub.chatroom.di

import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.domain.usecase.ChatAttachmentUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import dagger.Module
import dagger.Provides

@Module
class ChatListFakeUseCaseModule(
        private val getChatUseCase: GetChatUseCase,
        private val chatAttachmentUseCase: ChatAttachmentUseCase
) {

    @Provides
    @ChatScope
    fun provideGetChatUseCase(): GetChatUseCase = getChatUseCase

    @Provides
    @ChatScope
    fun provideChatAttachmentUseCase(): ChatAttachmentUseCase = chatAttachmentUseCase
}