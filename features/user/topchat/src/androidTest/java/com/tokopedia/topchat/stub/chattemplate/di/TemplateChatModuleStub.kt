package com.tokopedia.topchat.stub.chattemplate.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepository
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepositoryImpl
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepositoryImpl
import com.tokopedia.topchat.chattemplate.data.source.CloudEditTemplateChatDataSource
import com.tokopedia.topchat.chattemplate.data.source.CloudGetTemplateChatDataSource
import com.tokopedia.topchat.chattemplate.domain.usecase.*
import com.tokopedia.topchat.common.chat.api.ChatTemplateApi
import com.tokopedia.topchat.stub.chatroom.di.ChatNetworkModuleStub
import com.tokopedia.topchat.stub.chattemplate.usecase.*
import com.tokopedia.topchat.stub.chattemplate.usecase.api.ChatTemplateApiStub
import dagger.Module
import dagger.Provides

@Module(
    includes = [ChatNetworkModuleStub::class, MediaUploaderModule::class]
)
class TemplateChatModuleStub {

    @ActivityScope
    @Provides
    fun provideTemplateApiStub(): ChatTemplateApiStub {
        return ChatTemplateApiStub()
    }

    @ActivityScope
    @Provides
    fun provideCloudGetTemplateChatDataSource(
        chatTemplateApi: ChatTemplateApiStub
    ): CloudGetTemplateChatDataSource {
        return CloudGetTemplateChatDataSource(chatTemplateApi)
    }

    @ActivityScope
    @Provides
    fun provideCloudEditTemplateChatDataSource(
        chatTemplateApi: ChatTemplateApiStub
    ): CloudEditTemplateChatDataSource {
        return CloudEditTemplateChatDataSource(chatTemplateApi)
    }

    // -- Separator --//

    @ActivityScope
    @Provides
    fun provideTemplateRepositoryImpl(
        cloudGetTemplateChatDataSource: CloudGetTemplateChatDataSource
    ): TemplateRepositoryImpl {
        return TemplateRepositoryImpl(cloudGetTemplateChatDataSource)
    }

    @ActivityScope
    @Provides
    fun provideTemplateRepository(
        templateRepositoryImpl: TemplateRepositoryImpl
    ): TemplateRepository {
        return templateRepositoryImpl
    }

    // -- Separator --//

    @ActivityScope
    @Provides
    fun provideEditTemplateRepositoryImpl(
        cloudEditTemplateChatDataSource: CloudEditTemplateChatDataSource
    ): EditTemplateRepositoryImpl {
        return EditTemplateRepositoryImpl(cloudEditTemplateChatDataSource)
    }

    @ActivityScope
    @Provides
    fun provideEditTemplateRepository(
        editTemplateRepositoryImpl: EditTemplateRepositoryImpl
    ): EditTemplateRepository {
        return editTemplateRepositoryImpl
    }

    // -- Separator --//

    @ActivityScope
    @Provides
    fun provideGetTemplateUseCaseStub(
        chatTemplateApi: ChatTemplateApiStub,
        templateRepository: TemplateRepository,
        dispatchers: CoroutineDispatchers
    ): GetTemplateUseCaseStub {
        return GetTemplateUseCaseStub(
            chatTemplateApi,
            templateRepository,
            dispatchers
        )
    }

    @ActivityScope
    @Provides
    fun provideGetTemplateUseCase(
        getTemplateUseCaseStub: GetTemplateUseCaseStub
    ): GetTemplateUseCase {
        return getTemplateUseCaseStub
    }

    // -- Separator --//

    @ActivityScope
    @Provides
    fun provideSetAvailabilityTemplateUseCaseStub(
        chatTemplateApi: ChatTemplateApiStub,
        templateRepository: TemplateRepository,
        dispatchers: CoroutineDispatchers
    ): SetAvailabilityTemplateUseCaseStub {
        return SetAvailabilityTemplateUseCaseStub(
            chatTemplateApi,
            templateRepository,
            dispatchers
        )
    }

    @ActivityScope
    @Provides
    fun provideSetAvailabilityTemplateUseCase(
        setAvailabilityTemplateUseCaseStub: SetAvailabilityTemplateUseCaseStub
    ): SetAvailabilityTemplateUseCase {
        return setAvailabilityTemplateUseCaseStub
    }

    // -- Separator --//

    @ActivityScope
    @Provides
    fun provideCreateTemplateUseCaseStub(
        chatTemplateApi: ChatTemplateApiStub,
        templateRepository: EditTemplateRepository,
        dispatchers: CoroutineDispatchers
    ): CreateTemplateUseCaseStub {
        return CreateTemplateUseCaseStub(
            chatTemplateApi,
            templateRepository,
            dispatchers
        )
    }

    @ActivityScope
    @Provides
    fun provideCreateTemplateUseCase(
        createTemplateUseCaseStub: CreateTemplateUseCaseStub
    ): CreateTemplateUseCase {
        return createTemplateUseCaseStub
    }

    // -- Separator --//

    @ActivityScope
    @Provides
    fun provideEditTemplateUseCaseStub(
        chatTemplateApi: ChatTemplateApiStub,
        templateRepository: EditTemplateRepository,
        dispatchers: CoroutineDispatchers
    ): EditTemplateUseCaseStub {
        return EditTemplateUseCaseStub(
            chatTemplateApi,
            templateRepository,
            dispatchers
        )
    }

    @ActivityScope
    @Provides
    fun provideEditTemplateUseCase(
        editTemplateUseCaseStub: EditTemplateUseCaseStub
    ): EditTemplateUseCase {
        return editTemplateUseCaseStub
    }

    // -- Separator --//

    @ActivityScope
    @Provides
    fun provideDeleteTemplateUseCaseStub(
        chatTemplateApi: ChatTemplateApiStub,
        templateRepository: EditTemplateRepository,
        dispatchers: CoroutineDispatchers
    ): DeleteTemplateUseCaseStub {
        return DeleteTemplateUseCaseStub(
            chatTemplateApi,
            templateRepository,
            dispatchers
        )
    }

    @ActivityScope
    @Provides
    fun provideDeleteTemplateUseCase(
        deleteTemplateUseCaseStub: DeleteTemplateUseCaseStub
    ): DeleteTemplateUseCase {
        return deleteTemplateUseCaseStub
    }
}