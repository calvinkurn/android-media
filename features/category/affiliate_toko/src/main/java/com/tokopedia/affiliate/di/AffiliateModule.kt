package com.tokopedia.affiliate.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.affiliate.sse.AffiliateSSE
import com.tokopedia.affiliate.sse.AffiliateSSEImpl
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class AffiliateModule {
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository =
        GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @AffiliateScope
    @Provides
    fun provideAffiliateSSE(
        @ApplicationContext appContext: Context,
        userSession: UserSessionInterface,
        dispatchers: CoroutineDispatchers
    ): AffiliateSSE =
        AffiliateSSEImpl(userSession, dispatchers, appContext)

    @AffiliateScope
    @Provides
    fun provideAbTestPlatform(): RemoteConfigInstance = RemoteConfigInstance.getInstance()
}
