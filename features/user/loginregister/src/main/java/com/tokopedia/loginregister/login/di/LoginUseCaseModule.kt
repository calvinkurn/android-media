package com.tokopedia.loginregister.login.di

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.sessioncommon.domain.usecase.LoginFingerprintUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenV2UseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Ade Fulki on 2019-10-09.
 * ade.hadian@tokopedia.com
 */

@Module
class LoginUseCaseModule {

    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @Provides
    fun provideRegisterCheckGraphQlUseCase(@ApplicationContext graphqlRepository: GraphqlRepository): GraphqlUseCase<RegisterCheckPojo> =
        GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideLoginTokenUseCaseV2(@ApplicationContext graphqlRepository: GraphqlRepository, userSessionInterface: UserSessionInterface): LoginTokenV2UseCase {
        val useCase = GraphqlUseCase<LoginTokenPojoV2>(graphqlRepository)
        return LoginTokenV2UseCase(useCase, userSessionInterface)
    }

    @Provides
    fun provideActivateUserUseCase(@ApplicationContext graphqlRepository: GraphqlRepository): ActivateUserUseCase {
        val useCase = GraphqlUseCase<ActivateUserPojo>(graphqlRepository)
        return ActivateUserUseCase(useCase)
    }

    @Provides
    fun provideLoginFingerprintUsecase(
        @ApplicationContext graphqlRepository: GraphqlRepository,
        dispatchers: CoroutineDispatchers,
        userSessionInterface: UserSessionInterface,
        fingerprintPreference: FingerprintPreference
    ): LoginFingerprintUseCase {
        val useCase = GraphqlUseCase<LoginTokenPojo>(graphqlRepository)
        return LoginFingerprintUseCase(useCase, dispatchers, userSessionInterface, fingerprintPreference)
    }
}
