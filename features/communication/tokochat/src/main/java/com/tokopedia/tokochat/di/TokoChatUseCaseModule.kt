package com.tokopedia.tokochat.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.tokochat.domain.usecase.TokoChatRoomUseCase
import dagger.Module
import dagger.Provides

@Module
object TokoChatUseCaseModule {

    @ActivityScope
    @Provides
    fun provideTokoChatRoomUseCase(
        @TokoChatQualifier tokoChatRepository: TokoChatRepository
    ): TokoChatRoomUseCase {
        return TokoChatRoomUseCase(tokoChatRepository)
    }
}
