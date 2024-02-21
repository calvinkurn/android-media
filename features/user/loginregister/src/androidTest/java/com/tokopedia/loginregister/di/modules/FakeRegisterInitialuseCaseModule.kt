package com.tokopedia.loginregister.di.modules

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.stub.FakeGraphqlRepository
import dagger.Module
import dagger.Provides

@Module
class FakeRegisterInitialuseCaseModule {

    @ActivityScope
    @Provides
    fun provideFakeGraphql(@ApplicationContext repository: GraphqlRepository): FakeGraphqlRepository =
        repository as FakeGraphqlRepository
}
