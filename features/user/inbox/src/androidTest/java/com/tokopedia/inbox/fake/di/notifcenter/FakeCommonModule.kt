package com.tokopedia.inbox.fake.di.notifcenter

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.inbox.fake.common.FakeUserSession
import com.tokopedia.notifcenter.di.scope.NotificationContext
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.util.CacheManager
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class FakeCommonModule {

    @Provides
    @NotificationContext
    fun provideNotificationContext(): Context {
        return InstrumentationRegistry.getInstrumentation().context
    }

    @Provides
    @NotificationScope
    fun provideCacheManager(@NotificationContext context: Context): CacheManager {
        return CacheManager(context)
    }

    @Provides
    @NotificationScope
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @NotificationScope
    fun provideUserSession(fake: FakeUserSession): UserSessionInterface {
        return fake
    }

    @Provides
    @NotificationScope
    fun provideFakeUserSession(@NotificationContext context: Context): FakeUserSession {
        return FakeUserSession(context)
    }
}