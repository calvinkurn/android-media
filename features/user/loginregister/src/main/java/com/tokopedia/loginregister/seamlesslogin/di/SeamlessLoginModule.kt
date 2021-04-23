package com.tokopedia.loginregister.seamlesslogin.di

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by nisie on 10/25/18.
 */
@Module
class SeamlessLoginModule(val context: Context) {

    @Provides
    @SeamlessLoginContext
    fun provideContext(): Context = context

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @SeamlessLoginScope
    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase {
        return GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
    }

    @SeamlessLoginScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @SeamlessLoginScope
    @Provides
    fun provideCoroutineDispatchersProvider(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }

    @SeamlessLoginScope
    @Provides
    fun provideUserSessionInterface(@SeamlessLoginContext context: Context): UserSessionInterface = UserSession(context)

}
