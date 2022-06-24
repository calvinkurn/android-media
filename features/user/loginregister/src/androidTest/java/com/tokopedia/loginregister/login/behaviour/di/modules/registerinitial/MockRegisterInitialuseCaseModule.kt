package com.tokopedia.loginregister.login.behaviour.di.modules.registerinitial

import android.content.res.Resources
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.external_register.ovo.data.CheckOvoResponse
import com.tokopedia.loginregister.external_register.ovo.domain.usecase.CheckHasOvoAccUseCase
import com.tokopedia.loginregister.login.behaviour.data.DiscoverUseCaseStub
import com.tokopedia.loginregister.login.behaviour.data.GeneratePublicKeyUseCaseStub
import com.tokopedia.loginregister.login.behaviour.data.GetProfileUseCaseStub
import com.tokopedia.loginregister.login.behaviour.data.GraphqlUseCaseStub
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestV2
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import dagger.Module
import dagger.Provides

@Module
class MockRegisterInitialuseCaseModule {

    @Provides
    fun provideRegisterRequestGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<RegisterRequestPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideRegisterRequestV2GraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<RegisterRequestV2> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideCheckHasOvoUseCase(graphqlRepository: GraphqlRepository): CheckHasOvoAccUseCase {
        val useCase = GraphqlUseCase<CheckOvoResponse>(graphqlRepository)
        return CheckHasOvoAccUseCase(useCase)
    }

    @ActivityScope
    @Provides
    fun provideDiscoverUseCase(
        stub: DiscoverUseCaseStub
    ): DiscoverUseCase = stub

    @ActivityScope
    @Provides
    fun provideDiscoverUseCasStub(
        graphqlRepository: GraphqlRepository,
        coroutineDispatcher: CoroutineDispatchers
    ): DiscoverUseCaseStub {
        return DiscoverUseCaseStub(graphqlRepository, coroutineDispatcher)
    }

    @ActivityScope
    @Provides
    fun provideGeneratePublicUseCase(stub: GeneratePublicKeyUseCaseStub): GeneratePublicKeyUseCase = stub

    @ActivityScope
    @Provides
    fun provideGeneratePublicUseCaseStub(graphqlRepository: GraphqlRepository): GeneratePublicKeyUseCaseStub {
        val useCase = GraphqlUseCaseStub<GenerateKeyPojo>(graphqlRepository)
        return GeneratePublicKeyUseCaseStub(useCase)
    }

    @Provides
    @ActivityScope
    fun provideRegisterCheckUseCase(
        stub: GraphqlUseCaseStub<RegisterCheckPojo>
    ): GraphqlUseCase<RegisterCheckPojo> = stub

    @Provides
    @ActivityScope
    fun provideMockRegisterCheckUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCaseStub<RegisterCheckPojo> {
        return GraphqlUseCaseStub(graphqlRepository)
    }

    @Provides
    fun provideGetProfileUseCaseStub(resources: Resources,
                                     graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase): GetProfileUseCaseStub {
        return GetProfileUseCaseStub(resources, graphqlUseCase)
    }
}