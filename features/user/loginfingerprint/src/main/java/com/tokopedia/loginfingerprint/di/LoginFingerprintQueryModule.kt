package com.tokopedia.loginfingerprint.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.RemoveFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.VerifyFingerprintPojo
import com.tokopedia.loginfingerprint.domain.usecase.CheckFingerprintToggleStatusUseCase
import com.tokopedia.loginfingerprint.domain.usecase.RegisterFingerprintUseCase
import com.tokopedia.loginfingerprint.domain.usecase.RemoveFingerprintUsecase
import com.tokopedia.loginfingerprint.domain.usecase.VerifyFingerprintUseCase
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import dagger.Module
import dagger.Provides

@Module
class LoginFingerprintQueryModule {

    @LoginFingerprintSettingScope
    @Provides
    fun provideRegisterFingerprintUsecase(graphqlRepository: GraphqlRepository, dispatchers: CoroutineDispatchers, fingerprintPreference: FingerprintPreference): RegisterFingerprintUseCase {
        val useCase = GraphqlUseCase<RegisterFingerprintPojo>(graphqlRepository)
        return RegisterFingerprintUseCase(useCase, dispatchers, fingerprintPreference)
    }

    @LoginFingerprintSettingScope
    @Provides
    fun provideVerifyFingerprintUsecase(graphqlRepository: GraphqlRepository, dispatchers: CoroutineDispatchers, fingerprintPreference: FingerprintPreference): VerifyFingerprintUseCase {
        val useCase = GraphqlUseCase<VerifyFingerprintPojo>(graphqlRepository)
        return VerifyFingerprintUseCase(useCase, dispatchers, fingerprintPreference)
    }

    @LoginFingerprintSettingScope
    @Provides
    fun provideCheckFingerprintToggleUsecase(graphqlRepository: GraphqlRepository, dispatchers: CoroutineDispatchers, fingerprintPreference: FingerprintPreference): CheckFingerprintToggleStatusUseCase {
        val useCase = GraphqlUseCase<CheckFingerprintPojo>(graphqlRepository)
        return CheckFingerprintToggleStatusUseCase(useCase, dispatchers, fingerprintPreference)
    }

    @LoginFingerprintSettingScope
    @Provides
    fun provideLoginTokenUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<LoginTokenPojo> = GraphqlUseCase(graphqlRepository)

    @LoginFingerprintSettingScope
    @Provides
    fun provideRemoveFingerprintUseCase(graphqlRepository: GraphqlRepository, dispatchers: CoroutineDispatchers, fingerprintPreference: FingerprintPreference): RemoveFingerprintUsecase {
        val useCase = GraphqlUseCase<RemoveFingerprintPojo>(graphqlRepository)
        return RemoveFingerprintUsecase(useCase, dispatchers, fingerprintPreference)
    }

}
