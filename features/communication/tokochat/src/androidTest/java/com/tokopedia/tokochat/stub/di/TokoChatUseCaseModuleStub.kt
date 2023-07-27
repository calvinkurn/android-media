package com.tokopedia.tokochat.stub.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.domain.usecase.TokoChatChannelUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatSendMessageUseCase
import com.tokopedia.tokochat.stub.domain.usecase.TokoChatChannelUseCaseStub
import com.tokopedia.tokochat.stub.domain.usecase.TokoChatSendMessageUseCaseStub
import com.tokopedia.tokochat.stub.repository.TokoChatRepositoryStub
import dagger.Module
import dagger.Provides

@Module
object TokoChatUseCaseModuleStub {

    @ActivityScope
    @Provides
    fun provideTokoChatChannelUseCaseStub(
        @TokoChatQualifier tokoChatRepositoryStub: TokoChatRepositoryStub
    ): TokoChatChannelUseCaseStub {
        return TokoChatChannelUseCaseStub(tokoChatRepositoryStub)
    }

    @ActivityScope
    @Provides
    fun provideTokoChatChannelUseCase(
        @ActivityScope tokoChatChannelUseCaseStub: TokoChatChannelUseCaseStub
    ): TokoChatChannelUseCase {
        return tokoChatChannelUseCaseStub
    }

    @ActivityScope
    @Provides
    fun provideTokoChatSendMessageUseCaseStub(
        @TokoChatQualifier tokoChatRepositoryStub: TokoChatRepositoryStub
    ): TokoChatSendMessageUseCaseStub {
        return TokoChatSendMessageUseCaseStub(tokoChatRepositoryStub)
    }

    @ActivityScope
    @Provides
    fun provideTokoChatSendMessageUseCase(
        @ActivityScope tokoChatSendMessageUseCaseStub: TokoChatSendMessageUseCaseStub
    ): TokoChatSendMessageUseCase {
        return tokoChatSendMessageUseCaseStub
    }
}
