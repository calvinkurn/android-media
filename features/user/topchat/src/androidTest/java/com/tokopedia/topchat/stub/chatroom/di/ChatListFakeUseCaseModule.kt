package com.tokopedia.topchat.stub.chatroom.di

import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import dagger.Module
import dagger.Provides

@Module
class ChatListFakeUseCaseModule(
        private val getChatUseCase: GetChatUseCase
) {

    @Provides
    @ChatScope
    fun provideGetChatUseCase(): GetChatUseCase = getChatUseCase
}