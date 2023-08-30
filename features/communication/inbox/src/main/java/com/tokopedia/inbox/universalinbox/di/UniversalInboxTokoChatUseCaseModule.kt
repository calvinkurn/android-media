package com.tokopedia.inbox.universalinbox.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.universalinbox.domain.UniversalInboxGetAllDriverChannelsUseCase
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import dagger.Module
import dagger.Provides

@Module
object UniversalInboxTokoChatUseCaseModule {
    @ActivityScope
    @Provides
    fun provideGetAllDriverChannelUseCase(
        @TokoChatQualifier tokoChatRepository: TokoChatRepository
    ): UniversalInboxGetAllDriverChannelsUseCase {
        return UniversalInboxGetAllDriverChannelsUseCase(tokoChatRepository)
    }
}
