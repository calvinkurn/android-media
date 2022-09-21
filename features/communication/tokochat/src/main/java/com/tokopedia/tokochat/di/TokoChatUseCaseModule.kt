package com.tokopedia.tokochat.di

import com.tokopedia.tokochat.data.repository.TokoChatRepository
import com.tokopedia.tokochat.domain.CreateChannelUseCase
import com.tokopedia.tokochat.domain.GetAllChannelsUseCase
import com.tokopedia.tokochat.domain.GetChatHistoryUseCase
import com.tokopedia.tokochat.domain.MarkAsReadUseCase
import com.tokopedia.tokochat.domain.RegistrationActiveChannelUseCase
import com.tokopedia.tokochat.domain.SendMessageUseCase
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
