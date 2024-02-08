package com.tokopedia.loginregister.di.modules

import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.stub.FakeGraphqlRepository
import com.tokopedia.loginregister.stub.usecase.GetProfileUseCaseStub
import dagger.Module
import dagger.Provides

@Module
class MockRegisterInitialuseCaseModule {

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
