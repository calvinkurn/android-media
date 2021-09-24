package com.tokopedia.updateinactivephone.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.domain.api.InactivePhoneApi
import com.tokopedia.updateinactivephone.domain.api.InactivePhoneApiClient
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneSubmitDataModel
import com.tokopedia.updateinactivephone.domain.data.PhoneValidationDataModel
import com.tokopedia.updateinactivephone.domain.usecase.*
import dagger.Module
import dagger.Provides

@Module
class InactivePhoneUseCaseModule {

    @ActivityScope
    @Provides
    fun provideGetAccountListUseCase(
        repository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
    ): GetAccountListUseCase {
        return GetAccountListUseCase(repository, dispatcher)
    }

    @ActivityScope
    @Provides
    fun providePhoneValidationUseCase(
        repository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
    ): PhoneValidationUseCase {
        return PhoneValidationUseCase(repository, dispatcher)
    }

    @ActivityScope
    @Provides
    fun provideSubmitDataUseCase(
        repository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
    ): SubmitDataUseCase {
        return SubmitDataUseCase(repository, dispatcher)
    }

    @ActivityScope
    @Provides
    fun provideImageUploadUseCase(
        apiClient: InactivePhoneApiClient<InactivePhoneApi>
    ): ImageUploadUseCase {
        return ImageUploadUseCase(apiClient)
    }

    @ActivityScope
    @Provides
    fun provideGetStatusInactivePhoneNumberUseCase(
        repository: GraphqlRepository,
        dispatchers: CoroutineDispatchers
    ): GetStatusInactivePhoneNumberUseCase {
        return GetStatusInactivePhoneNumberUseCase(repository, dispatchers)
    }

    @ActivityScope
    @Provides
    fun provideSubmitExpeditedInactivePhoneUseCase(
        repository: GraphqlRepository,
        dispatchers: CoroutineDispatchers
    ): SubmitExpeditedInactivePhoneUseCase {
        return SubmitExpeditedInactivePhoneUseCase(repository, dispatchers)
    }

}