package com.tokopedia.chat_service.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_service.data.repository.ChatServiceRepository
import com.tokopedia.chat_service.domain.CreateChannelUseCase
import com.tokopedia.chat_service.domain.GetChatHistoryUseCase
import dagger.Module
import dagger.Provides

@Module
object ChatServiceUseCaseModule {

    @ChatServiceScope
    @Provides
    fun provideCreateChannelUseCase(
        repository: ChatServiceRepository,
        coroutineDispatchers: CoroutineDispatchers
    ): CreateChannelUseCase {
        return CreateChannelUseCase(repository, coroutineDispatchers)
    }

    @ChatServiceScope
    @Provides
    fun provideGetChatHistoryUseCase(
        repository: ChatServiceRepository,
        coroutineDispatchers: CoroutineDispatchers
    ): GetChatHistoryUseCase {
        return GetChatHistoryUseCase(repository, coroutineDispatchers)
    }
}