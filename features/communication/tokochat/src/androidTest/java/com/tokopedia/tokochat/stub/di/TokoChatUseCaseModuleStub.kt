package com.tokopedia.tokochat.stub.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.domain.usecase.TokoChatRoomUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatSendMessageUseCase
import com.tokopedia.tokochat.stub.domain.usecase.TokoChatRoomUseCaseStub
import com.tokopedia.tokochat.stub.domain.usecase.TokoChatSendMessageUseCaseStub
import com.tokopedia.tokochat.stub.repository.TokoChatRepositoryStub
import dagger.Module
import dagger.Provides

@Module
object TokoChatUseCaseModuleStub {

    @ActivityScope
    @Provides
    fun provideTokoChatRoomUseCaseStub(
        @TokoChatQualifier tokoChatRepositoryStub: TokoChatRepositoryStub
    ): TokoChatRoomUseCaseStub {
        return TokoChatRoomUseCaseStub(tokoChatRepositoryStub)
    }

    @ActivityScope
    @Provides
    fun provideTokoChatRoomUseCase(
        @ActivityScope tokoChatRoomUseCaseStub: TokoChatRoomUseCaseStub
    ): TokoChatRoomUseCase {
        return tokoChatRoomUseCaseStub
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
