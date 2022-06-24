package com.tokopedia.loginregister.login.behaviour.di.modules

import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.login.behaviour.data.*
import com.tokopedia.loginregister.login.domain.RegisterCheckUseCase
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenV2UseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Ade Fulki on 2019-10-09.
 * ade.hadian@tokopedia.com
 */

@Module
class LoginUseCaseModuleStub {

    @ActivityScope
    @Provides
    fun provideGeneratePublicUseCase(stub: GeneratePublicKeyUseCaseStub): GeneratePublicKeyUseCase =
        stub

    @ActivityScope
    @Provides
    fun provideGeneratePublicUseCaseStub(@ApplicationContext graphqlRepository: GraphqlRepository): GeneratePublicKeyUseCaseStub {
        val useCase = GraphqlUseCaseStub<GenerateKeyPojo>(graphqlRepository)
        return GeneratePublicKeyUseCaseStub(useCase)
    }

    @ActivityScope
    @Provides
    fun provideLoginTokenUseCaseV2(stub: LoginTokenV2UseCaseStub): LoginTokenV2UseCase = stub

    @ActivityScope
    @Provides
    fun provideLoginTokenUseCaseV2Stub(
        @ApplicationContext graphqlRepository: GraphqlRepository,
        userSessionInterface: UserSessionInterface
    ): LoginTokenV2UseCaseStub {
        val useCase = GraphqlUseCaseStub<LoginTokenPojoV2>(graphqlRepository)
        return LoginTokenV2UseCaseStub(useCase, userSessionInterface)
    }

    @Provides
    @ActivityScope
    fun provideRegisterCheckUseCase(
        stub: RegisterCheckUseCaseStub
    ): RegisterCheckUseCase = stub

    @ActivityScope
    @Provides
    fun provideRegisterCheckGraphQlUseCase(@ApplicationContext graphqlRepository: GraphqlRepository): RegisterCheckUseCaseStub {
        return RegisterCheckUseCaseStub(graphqlRepository)
    }

    @Provides
    @ActivityScope
    fun provideLoginTokenUseCase(
        stub: LoginTokenUseCaseStub
    ): LoginTokenUseCase = stub

    @ActivityScope
    @Provides
    fun provideLoginTokenUseCaseStub(
        resources: Resources,
        graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase,
        userSessionInterface: UserSessionInterface
    ): LoginTokenUseCaseStub {
        return LoginTokenUseCaseStub(resources, graphqlUseCase, userSessionInterface)
    }

    @Provides
    @ActivityScope
    fun provideActivateUserUseCase(@ApplicationContext graphqlRepository: GraphqlRepository): ActivateUserUseCase {
        val useCase = GraphqlUseCase<ActivateUserPojo>(graphqlRepository)
        return ActivateUserUseCase(useCase)
    }

    @Provides
    @ActivityScope
    fun provideGetProfileUseCase(
        stub: GetProfileUseCaseStub
    ): GetProfileUseCase = stub

    @ActivityScope
    @Provides
    fun provideGetProfileUseCaseStub(
        resources: Resources,
        graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase
    ): GetProfileUseCaseStub {
        return GetProfileUseCaseStub(resources, graphqlUseCase)
    }

    @Provides
    @ActivityScope
    fun provideDiscoverUseCase(
        stub: DiscoverUseCaseStub
    ): DiscoverUseCase = stub

    @ActivityScope
    @Provides
    fun provideDiscoverUseCasStub(
        @ApplicationContext graphqlRepository: GraphqlRepository,
        coroutineDispatcher: CoroutineDispatchers
    ): DiscoverUseCaseStub {
        return DiscoverUseCaseStub(graphqlRepository, coroutineDispatcher)
    }

    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase =
        GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
}