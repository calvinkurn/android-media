package com.tokopedia.updateinactivephone.stub.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.domain.usecase.*
import com.tokopedia.updateinactivephone.stub.domain.usecase.*
import dagger.Module
import dagger.Provides

@Module
class FakeInactivePhoneUseCaseModule {

    @ActivityScope
    @Provides
    fun provideGetAccountListUseCase(
        stub: GetAccountListUseCaseStub
    ): GetAccountListUseCase = stub

    @ActivityScope
    @Provides
    fun provideGetAccountListUseCaseStub(
        repository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
    ): GetAccountListUseCaseStub {
        return GetAccountListUseCaseStub(repository, dispatcher)
    }

    @ActivityScope
    @Provides
    fun provideGetStatusInactivePhoneNumberUseCase(
        stub: GetStatusInactivePhoneNumberUseCaseStub
    ): GetStatusInactivePhoneNumberUseCase = stub

    @ActivityScope
    @Provides
    fun provideGetStatusInactivePhoneNumberUseCaseStub(
        repository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
    ): GetStatusInactivePhoneNumberUseCaseStub {
        return GetStatusInactivePhoneNumberUseCaseStub(repository, dispatcher)
    }

    @ActivityScope
    @Provides
    fun providePhoneValidationUseCase(
        stub: PhoneValidationUseCaseStub
    ): PhoneValidationUseCase = stub

    @ActivityScope
    @Provides
    fun providePhoneValidationUseCaseStub(
        repository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
    ): PhoneValidationUseCaseStub {
        return PhoneValidationUseCaseStub(repository, dispatcher)
    }

    @ActivityScope
    @Provides
    fun provideSubmitDataUseCase(
        stub: SubmitDataUseCaseStub
    ): SubmitDataUseCase = stub

    @ActivityScope
    @Provides
    fun provideSubmitDataUseCaseStub(
        repository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
    ): SubmitDataUseCaseStub {
        return SubmitDataUseCaseStub(repository, dispatcher)
    }

    @ActivityScope
    @Provides
    fun provideSubmitExpeditedInactivePhoneUseCase(
        stub: SubmitExpeditedInactivePhoneUseCaseStub
    ): SubmitExpeditedInactivePhoneUseCase = stub

    @ActivityScope
    @Provides
    fun provideSubmitExpeditedInactivePhoneUseCaseStub(
        repository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
    ): SubmitExpeditedInactivePhoneUseCaseStub {
        return SubmitExpeditedInactivePhoneUseCaseStub(repository, dispatcher)
    }

    @ActivityScope
    @Provides
    fun provideVerifyNewPhoneUseCase(
        stub: VerifyNewPhoneUseCaseStub
    ): VerifyNewPhoneUseCase = stub

    @ActivityScope
    @Provides
    fun provideVerifyNewPhoneUseCaseStub(
        repository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
    ): VerifyNewPhoneUseCaseStub {
        return VerifyNewPhoneUseCaseStub(repository, dispatcher)
    }
}