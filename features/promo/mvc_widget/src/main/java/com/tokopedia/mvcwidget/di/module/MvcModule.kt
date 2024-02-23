package com.tokopedia.mvcwidget.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mvcwidget.IO
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class MvcModule {
    @Provides
    @Named(IO)
    fun provideWorkerDispatcher(): CoroutineDispatcher = Dispatchers.IO


    @Provides
    fun provideSession(@ApplicationContext context: Context): UserSessionInterface =
        UserSession(context)

    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

}
