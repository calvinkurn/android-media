package com.tokopedia.loginregister.login.behaviour.di.modules.registerinitial

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.login.stub.RegisterInitialRouterHelperStub
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialScope
import com.tokopedia.loginregister.registerinitial.view.util.RegisterInitialRouterHelper
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MockRegisterInitialModule {
    @Provides
    fun providesContext(@ApplicationContext context: Context): Context = context

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @RegisterInitialScope
    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase {
        return GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
    }

    @RegisterInitialScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Provides
    @RegisterInitialScope
    fun provideRegisterInitialRouter(stub: RegisterInitialRouterHelperStub): RegisterInitialRouterHelper {
        return stub
    }

    @Provides
    @RegisterInitialScope
    fun provideRegisterInitialRouter(): RegisterInitialRouterHelperStub {
        return RegisterInitialRouterHelperStub()
    }
}