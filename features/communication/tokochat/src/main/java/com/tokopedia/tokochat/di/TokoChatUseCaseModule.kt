package com.tokopedia.tokochat.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.domain.TokoChatChannelUseCase
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.tokochat.domain.usecase.TokoChatRoomUseCase
import dagger.Module
import dagger.Provides

@Module
object TokoChatUseCaseModule {

    @ActivityScope
    @Provides
    fun provideTokoChatRoomUseCase(
        @TokoChatQualifier tokoChatRepository: TokoChatRepository,
        tokoChatChannelUseCase: TokoChatChannelUseCase
    ): TokoChatRoomUseCase {
        return TokoChatRoomUseCase(tokoChatRepository, tokoChatChannelUseCase)
    }

    @ActivityScope
    @Provides
    fun provideTokoChatChannelUseCase(
        @TokoChatQualifier tokoChatChannelUseCase: TokoChatChannelUseCase
    ): TokoChatChannelUseCase {
        return tokoChatChannelUseCase
    }
}
