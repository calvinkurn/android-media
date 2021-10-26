package com.tokopedia.loginregister.registerpushnotif.di

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.common.di.LoginRegisterScope
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

    @LoginRegisterScope
    @Provides
    @Named(SessionModule.SESSION_MODULE)
    fun provideUserSession(): UserSessionInterface = UserSession(context)

    @LoginRegisterScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @LoginRegisterScope
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchersProvider
}