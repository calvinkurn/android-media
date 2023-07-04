package com.tokopedia.notifcenter.di.module

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifcenter.di.scope.NotificationContext
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.util.cache.NotifCenterCacheManager
import com.tokopedia.notifcenter.util.cache.NotifCenterCacheManagerImpl
import com.tokopedia.notifcenter.util.cache.NotifCenterCacheManagerImpl.Companion.PREF_NOTIF_CENTER
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module class CommonModule(val context: Context) {

    @Provides
    @NotificationContext
    fun provideNotificationContext(): Context {
        return context
    }

    @Provides
    @NotificationScope
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @Provides
    @NotificationScope
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @NotificationScope
    fun provideUserSession(@NotificationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @NotificationScope
    internal fun provideNotifCenterSharedPref(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            PREF_NOTIF_CENTER,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @NotificationScope
    internal fun provideNotifCenterCacheManager(
        sharedPreferences: SharedPreferences
    ): NotifCenterCacheManager {
        return NotifCenterCacheManagerImpl(sharedPreferences)
    }
}
