package com.tokopedia.inbox.fake.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.inbox.di.InboxScope
import com.tokopedia.inbox.domain.cache.InboxCacheManager
import com.tokopedia.inbox.fake.common.FakeUserSession
import com.tokopedia.inbox.fake.domain.cache.FakeInboxCacheManager
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * equivalent to
 * [com.tokopedia.inbox.di.InboxCommonModule]
 */
@Module
class FakeInboxCommonModule {

    @InboxScope
    @Provides
    fun provideUserSession(
            fakeSession: FakeUserSession
    ): UserSessionInterface {
        return fakeSession
    }

    @InboxScope
    @Provides
    fun provideFakeUserSession(
            @ApplicationContext context: Context
    ): FakeUserSession {
        return FakeUserSession(context)
    }

    // -- separator -- //

    @InboxScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    // -- separator -- //

    @InboxScope
    @Provides
    internal fun provideInboxCacheManager(
            fakeInboxCacheManager: FakeInboxCacheManager
    ): InboxCacheManager {
        return fakeInboxCacheManager
    }

    @InboxScope
    @Provides
    internal fun provideFakeInboxCacheManager(): FakeInboxCacheManager {
        return FakeInboxCacheManager()
    }

}