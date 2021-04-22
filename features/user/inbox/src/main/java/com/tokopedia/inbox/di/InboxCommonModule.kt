package com.tokopedia.inbox.di

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.inbox.domain.cache.InboxCacheManager
import com.tokopedia.inbox.domain.cache.InboxCacheManagerImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class InboxCommonModule {

    @InboxScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @InboxScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @InboxScope
    @Provides
    fun provideInboxCoroutineDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider

    @InboxScope
    @Provides
    internal fun provideInboxSharedPref(
            @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
                "prefs_inbox_v2",
                Context.MODE_PRIVATE
        )
    }

    @InboxScope
    @Provides
    internal fun provideInboxCacheManager(
            sharedPreferences: SharedPreferences
    ): InboxCacheManager {
        return InboxCacheManagerImpl(sharedPreferences)
    }
}