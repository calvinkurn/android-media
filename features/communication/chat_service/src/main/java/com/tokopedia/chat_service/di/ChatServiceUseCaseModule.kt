package com.tokopedia.chat_service.di

import com.tokopedia.chat_service.data.repository.ChatServiceRepository
import com.tokopedia.chat_service.domain.CreateChannelUseCase
import com.tokopedia.chat_service.domain.GetAllChannelsUseCase
import com.tokopedia.chat_service.domain.GetChatHistoryUseCase
import com.tokopedia.chat_service.domain.MarkAsReadUseCase
import com.tokopedia.chat_service.domain.RegistrationActiveChannelUseCase
import com.tokopedia.chat_service.domain.SendMessageUseCase
import dagger.Module
import dagger.Provides

@Module
object ChatServiceUseCaseModule {

    @ChatServiceScope
    @Provides
    fun provideCreateChannelUseCase(
        repository: ChatServiceRepository
    ): CreateChannelUseCase {
        return CreateChannelUseCase(repository)
    }

    @ChatServiceScope
    @Provides
    fun provideGetChatHistoryUseCase(
        repository: ChatServiceRepository
    ): GetChatHistoryUseCase {
        return GetChatHistoryUseCase(repository)
    }

    @ChatServiceScope
    @Provides
    fun provideGetAllChannelsUseCase(
        repository: ChatServiceRepository
    ): GetAllChannelsUseCase {
        return GetAllChannelsUseCase(repository)
    }

    @ChatServiceScope
    @Provides
    fun provideMarkAsReadUseCase(
        repository: ChatServiceRepository
    ): MarkAsReadUseCase {
        return MarkAsReadUseCase(repository)
    }

    @ChatServiceScope
    @Provides
    fun provideRegistrationActiveChannelUseCase(
        repository: ChatServiceRepository
    ): RegistrationActiveChannelUseCase {
        return RegistrationActiveChannelUseCase(repository)
    }

    @ChatServiceScope
    @Provides
    fun provideSendMessageUseCase(
        repository: ChatServiceRepository
    ): SendMessageUseCase {
        return SendMessageUseCase(repository)
    }
}