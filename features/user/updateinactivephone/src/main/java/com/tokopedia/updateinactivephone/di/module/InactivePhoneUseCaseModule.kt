package com.tokopedia.updateinactivephone.di.module

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.di.InactivePhoneScope
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

    @InactivePhoneScope
    @Provides
    fun provideGetAccountListUseCase(useCase: GraphqlUseCase<AccountListDataModel>): GetAccountListUseCase {
        return GetAccountListUseCase(useCase)
    }

    @InactivePhoneScope
    @Provides
    fun providePhoneValidationUseCase(useCase: GraphqlUseCase<PhoneValidationDataModel>): PhoneValidationUseCase {
        return PhoneValidationUseCase(useCase)
    }

    @InactivePhoneScope
    @Provides
    fun provideSubmitDataUseCase(useCase: GraphqlUseCase<InactivePhoneSubmitDataModel>): SubmitDataUseCase {
        return SubmitDataUseCase(useCase)
    }

    @InactivePhoneScope
    @Provides
    fun provideImageUploadUseCase(apiClient: InactivePhoneApiClient<InactivePhoneApi>): ImageUploadUseCase {
        return ImageUploadUseCase(apiClient)
    }

    @InactivePhoneScope
    @Provides
    fun provideGetStatusInactivePhoneNumberUseCase(
        repository: GraphqlRepository,
        dispatchers: CoroutineDispatchers
    ): GetStatusInactivePhoneNumberUseCase {
        return GetStatusInactivePhoneNumberUseCase(repository, dispatchers)
    }

    @InactivePhoneScope
    @Provides
    fun provideSubmitExpeditedInactivePhoneUseCase(
        repository: GraphqlRepository,
        dispatchers: CoroutineDispatchers
    ): SubmitExpeditedInactivePhoneUseCase {
        return SubmitExpeditedInactivePhoneUseCase(repository, dispatchers)
    }

}