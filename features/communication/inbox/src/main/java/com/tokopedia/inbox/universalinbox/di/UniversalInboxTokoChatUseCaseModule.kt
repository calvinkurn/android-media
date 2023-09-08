package com.tokopedia.inbox.universalinbox.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.inbox.universalinbox.domain.usecase.UniversalInboxGetAllDriverChannelsUseCase
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import dagger.Module
import dagger.Provides

@Module
object UniversalInboxTokoChatUseCaseModule {
    @ActivityScope
    @Provides
    fun provideGetAllDriverChannelUseCase(
        @TokoChatQualifier tokoChatRepository: TokoChatRepository,
        dispatchers: CoroutineDispatchers
    ): UniversalInboxGetAllDriverChannelsUseCase {
        return UniversalInboxGetAllDriverChannelsUseCase(tokoChatRepository, dispatchers)
    }
}
