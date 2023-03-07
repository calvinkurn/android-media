package com.tokopedia.loginregister.registerpushnotif.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class RegisterPushNotificationModule(
    val context: Context
) {

    @ActivityScope
    @Provides
    @Named(SessionModule.SESSION_MODULE)
    fun provideUserSession(): UserSessionInterface = UserSession(context)

    @ActivityScope
    @ApplicationContext
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ActivityScope
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchersProvider
}