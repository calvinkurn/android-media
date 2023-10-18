package com.tokopedia.loginregister.di.modules

import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.stub.FakeGraphqlRepository
import com.tokopedia.loginregister.stub.usecase.GetAdminTypeUseCaseStub
import com.tokopedia.loginregister.stub.usecase.GetProfileUseCaseStub
import com.tokopedia.loginregister.stub.usecase.GraphqlUseCaseStub
import com.tokopedia.loginregister.stub.usecase.LoginTokenUseCaseStub
import com.tokopedia.loginregister.stub.usecase.LoginTokenV2UseCaseStub
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
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
    fun provideGetAdminTypeUseCase(
        graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase
    ): GetAdminTypeUseCase = GetAdminTypeUseCaseStub(graphqlUseCase)

    @ActivityScope
    @Provides
    fun provideGetAdminTypeUseCaseStub(graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase): GetAdminTypeUseCaseStub {
        return GetAdminTypeUseCaseStub(graphqlUseCase)
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
    ): LoginTokenUseCaseStub =
        LoginTokenUseCaseStub(resources, graphqlUseCase, userSessionInterface)

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
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase =
        GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @ActivityScope
    @Provides
    fun provideFakeRepository(@ApplicationContext repo: GraphqlRepository): FakeGraphqlRepository =
        repo as FakeGraphqlRepository
}
