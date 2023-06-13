package com.tokopedia.topchat.stub.chattemplate.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.domain.usecase.CreateTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.DeleteTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.GetTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.RearrangeTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.ToggleTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.UpdateTemplateUseCase
import com.tokopedia.topchat.stub.chattemplate.usecase.CreateTemplateUseCaseStub
import com.tokopedia.topchat.stub.chattemplate.usecase.DeleteTemplateUseCaseStub
import com.tokopedia.topchat.stub.chattemplate.usecase.GetTemplateUseCaseStub
import com.tokopedia.topchat.stub.chattemplate.usecase.RearrangeTemplateUseCaseStub
import com.tokopedia.topchat.stub.chattemplate.usecase.ToggleTemplateUseCaseStub
import com.tokopedia.topchat.stub.chattemplate.usecase.UpdateTemplateUseCaseStub
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import dagger.Module
import dagger.Provides

@Module
class ChatTemplateUseCaseStub {

    /**
     * Create Template
     */

    @ActivityScope
    @Provides
    fun provideCreateTemplateUseCaseStub(
        repositoryStub: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): CreateTemplateUseCaseStub {
        return CreateTemplateUseCaseStub(repositoryStub, dispatchers)
    }

    @ActivityScope
    @Provides
    fun provideCreateTemplateUseCase(
        useCaseStub: CreateTemplateUseCaseStub
    ): CreateTemplateUseCase {
        return useCaseStub
    }

    /**
     * Delete Template
     */

    @ActivityScope
    @Provides
    fun provideDeleteTemplateUseCaseStub(
        repositoryStub: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): DeleteTemplateUseCaseStub {
        return DeleteTemplateUseCaseStub(repositoryStub, dispatchers)
    }

    @ActivityScope
    @Provides
    fun provideDeleteTemplateUseCase(
        useCaseStub: DeleteTemplateUseCaseStub
    ): DeleteTemplateUseCase {
        return useCaseStub
    }

    /**
     * Get Template
     */

    @ActivityScope
    @Provides
    fun provideGetTemplateUseCaseStub(
        repositoryStub: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): GetTemplateUseCaseStub {
        return GetTemplateUseCaseStub(repositoryStub, dispatchers)
    }

    @ActivityScope
    @Provides
    fun provideGetTemplateUseCase(
        useCaseStub: GetTemplateUseCaseStub
    ): GetTemplateUseCase {
        return useCaseStub
    }

    /**
     * Rearrange Template
     */

    @ActivityScope
    @Provides
    fun provideRearrangeTemplateUseCaseStub(
        repositoryStub: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): RearrangeTemplateUseCaseStub {
        return RearrangeTemplateUseCaseStub(repositoryStub, dispatchers)
    }

    @ActivityScope
    @Provides
    fun provideRearrangeTemplateUseCase(
        useCaseStub: RearrangeTemplateUseCaseStub
    ): RearrangeTemplateUseCase {
        return useCaseStub
    }

    /**
     * Toggle Template
     */

    @ActivityScope
    @Provides
    fun provideToggleTemplateUseCaseStub(
        repositoryStub: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): ToggleTemplateUseCaseStub {
        return ToggleTemplateUseCaseStub(repositoryStub, dispatchers)
    }

    @ActivityScope
    @Provides
    fun provideToggleTemplateUseCase(
        useCaseStub: ToggleTemplateUseCaseStub
    ): ToggleTemplateUseCase {
        return useCaseStub
    }

    /**
     * Update Template
     */

    @ActivityScope
    @Provides
    fun provideUpdateTemplateUseCaseStub(
        repositoryStub: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): UpdateTemplateUseCaseStub {
        return UpdateTemplateUseCaseStub(repositoryStub, dispatchers)
    }

    @ActivityScope
    @Provides
    fun provideUpdateTemplateUseCase(
        useCaseStub: UpdateTemplateUseCaseStub
    ): UpdateTemplateUseCase {
        return useCaseStub
    }
}
