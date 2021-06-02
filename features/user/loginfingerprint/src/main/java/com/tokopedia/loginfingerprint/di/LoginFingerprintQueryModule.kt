package com.tokopedia.loginfingerprint.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.ValidateFingerprintPojo
import com.tokopedia.loginfingerprint.domain.usecase.RegisterFingerprintUseCase
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import dagger.Module
import dagger.Provides


@Module
class LoginFingerprintQueryModule {

    @LoginFingerprintSettingScope
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider

    @LoginFingerprintSettingScope
    @Provides
    fun provideRegisterFingerprintUsecase(graphqlRepository: GraphqlRepository, dispatchers: CoroutineDispatchers): RegisterFingerprintUseCase {
        val useCase = GraphqlUseCase<RegisterFingerprintPojo>(graphqlRepository)
        return RegisterFingerprintUseCase(useCase, dispatchers)
    }

    @LoginFingerprintSettingScope
    @Provides
    fun provideValidateFingerprintUsecase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<ValidateFingerprintPojo> = GraphqlUseCase(graphqlRepository)

    @LoginFingerprintSettingScope
    @Provides
    fun provideLoginTokenUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<LoginTokenPojo> = GraphqlUseCase(graphqlRepository)
}