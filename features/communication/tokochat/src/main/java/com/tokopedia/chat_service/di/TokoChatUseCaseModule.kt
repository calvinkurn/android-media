package com.tokopedia.chat_service.di

import com.tokopedia.chat_service.data.repository.TokoChatRepository
import com.tokopedia.chat_service.domain.CreateChannelUseCase
import com.tokopedia.chat_service.domain.GetAllChannelsUseCase
import com.tokopedia.chat_service.domain.GetChatHistoryUseCase
import com.tokopedia.chat_service.domain.MarkAsReadUseCase
import com.tokopedia.chat_service.domain.RegistrationActiveChannelUseCase
import com.tokopedia.chat_service.domain.SendMessageUseCase
import dagger.Module
import dagger.Provides

@Module
object TokoChatUseCaseModule {

    @TokoChatScope
    @Provides
    fun provideCreateChannelUseCase(
        repository: TokoChatRepository
    ): CreateChannelUseCase {
        return CreateChannelUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideGetChatHistoryUseCase(
        repository: TokoChatRepository
    ): GetChatHistoryUseCase {
        return GetChatHistoryUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideGetAllChannelsUseCase(
        repository: TokoChatRepository
    ): GetAllChannelsUseCase {
        return GetAllChannelsUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideMarkAsReadUseCase(
        repository: TokoChatRepository
    ): MarkAsReadUseCase {
        return MarkAsReadUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideRegistrationActiveChannelUseCase(
        repository: TokoChatRepository
    ): RegistrationActiveChannelUseCase {
        return RegistrationActiveChannelUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideSendMessageUseCase(
        repository: TokoChatRepository
    ): SendMessageUseCase {
        return SendMessageUseCase(repository)
    }
}
