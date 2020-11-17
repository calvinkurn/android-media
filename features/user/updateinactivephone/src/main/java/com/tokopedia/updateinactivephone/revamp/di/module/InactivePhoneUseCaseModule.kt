package com.tokopedia.updateinactivephone.revamp.di.module

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.updateinactivephone.revamp.di.InactivePhoneScope
import com.tokopedia.updateinactivephone.revamp.domain.api.InactivePhoneApi
import com.tokopedia.updateinactivephone.revamp.domain.api.InactivePhoneApiClient
import com.tokopedia.updateinactivephone.revamp.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.InactivePhoneSubmitDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.PhoneValidationDataModel
import com.tokopedia.updateinactivephone.revamp.domain.usecase.*
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
}