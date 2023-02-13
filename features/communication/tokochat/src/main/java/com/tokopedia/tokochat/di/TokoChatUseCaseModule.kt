package com.tokopedia.tokochat.di

import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.domain.usecase.TokoChatChannelUseCase
import dagger.Module
import dagger.Provides

@Module
object TokoChatUseCaseModule {

    @ActivityScope
    @Provides
    fun provideTokoChatChannelUseCase(
        @TokoChatQualifier tokoChatRepository: TokoChatRepository
    ): TokoChatChannelUseCase {
        return TokoChatChannelUseCase(tokoChatRepository)
    }
}
