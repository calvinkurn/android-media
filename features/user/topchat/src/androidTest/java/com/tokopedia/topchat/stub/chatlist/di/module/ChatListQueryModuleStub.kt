package com.tokopedia.topchat.stub.chatlist.di.module

import com.tokopedia.topchat.chatlist.di.ChatListScope
import com.tokopedia.topchat.chatlist.usecase.GetChatListMessageUseCase
import com.tokopedia.topchat.chatlist.usecase.GetChatNotificationUseCase
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatNotificationUseCaseStub
import dagger.Module
import dagger.Provides

@Module
class ChatListQueryModuleStub(
        private val chatListUseCase: GetChatListMessageUseCase,
        private val chatNotificationUseCaseStub: GetChatNotificationUseCaseStub
) {

    @Provides
    @ChatListScope
    fun provideGetChatListMessageInfoUseCase(): GetChatListMessageUseCase = chatListUseCase

    @Provides
    @ChatListScope
    fun provideGetChatNotificationUseCase(): GetChatNotificationUseCase = chatNotificationUseCaseStub
}