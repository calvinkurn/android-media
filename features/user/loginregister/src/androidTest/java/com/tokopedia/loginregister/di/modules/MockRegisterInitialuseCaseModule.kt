package com.tokopedia.loginregister.di.modules

import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestV2
import com.tokopedia.loginregister.stub.FakeGraphqlRepository
import com.tokopedia.loginregister.stub.usecase.GetProfileUseCaseStub
import com.tokopedia.loginregister.stub.usecase.GraphqlUseCaseStub
import dagger.Module
import dagger.Provides

@Module
class MockRegisterInitialuseCaseModule {

    @Provides
    fun provideRegisterRequestGraphQlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<RegisterRequestPojo> =
        GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideRegisterRequestV2GraphQlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<RegisterRequestV2> =
        GraphqlUseCase(graphqlRepository)

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
    fun provideGetProfileUseCaseStub(
        resources: Resources,
        graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase
    ): GetProfileUseCaseStub {
        return GetProfileUseCaseStub(resources, graphqlUseCase)
    }

    @ActivityScope
    @Provides
    fun provideFakeGraphql(@ApplicationContext repository: GraphqlRepository): FakeGraphqlRepository =
        repository as FakeGraphqlRepository
}
