package com.tokopedia.inbox.universalinbox.stub.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.universalinbox.domain.usecase.UniversalInboxGetAllDriverChannelsUseCase
import com.tokopedia.inbox.universalinbox.stub.domain.UniversalInboxGetAllDriverChannelsUseCaseStub
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import dagger.Module
import dagger.Provides

@Module
object UniversalInboxUseCaseModuleStub {

    @ActivityScope
    @Provides
    fun provideGetAllDriverUseCase(
        useCaseStub: UniversalInboxGetAllDriverChannelsUseCaseStub
    ): UniversalInboxGetAllDriverChannelsUseCase {
        return useCaseStub
    }

    @ActivityScope
    @Provides
    fun provideGetAllDriverUseCaseStub(
        @TokoChatQualifier tokoChatRepository: TokoChatRepository
    ): UniversalInboxGetAllDriverChannelsUseCaseStub {
        return UniversalInboxGetAllDriverChannelsUseCaseStub(tokoChatRepository)
    }
}
