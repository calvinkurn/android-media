package com.tokopedia.play.broadcaster.di.broadcast

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.pusher.PlayPusher
import com.tokopedia.play.broadcaster.pusher.PlayPusherBuilder
import com.tokopedia.play.broadcaster.socket.PlayBroadcastSocket
import com.tokopedia.play.broadcaster.socket.PlayBroadcastSocket.Companion.KEY_GROUP_CHAT_PREFERENCES
import com.tokopedia.play.broadcaster.socket.PlayBroadcastSocketImpl
import com.tokopedia.play.broadcaster.util.coroutine.CommonCoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by jegul on 20/05/20
 */
@Module
class PlayBroadcastModule(val mContext: Context) {

    @Provides
    fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider = CommonCoroutineDispatcherProvider()

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideLocalCacheHandler(): LocalCacheHandler {
        return LocalCacheHandler(mContext, KEY_GROUP_CHAT_PREFERENCES)
    }

    @Provides
    fun providePlayPusher(@ApplicationContext context: Context): PlayPusher {
        return PlayPusherBuilder(context).build()
    }

    @Provides
    fun providePlaySocket(userSession: UserSessionInterface, cacheHandler: LocalCacheHandler): PlayBroadcastSocket {
        return PlayBroadcastSocketImpl(userSession, cacheHandler)
    }

    @Provides
    fun provideGraphQLRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @PlayBroadcastScope
    @Provides
    fun providePlayBroadcastAnalytic(userSession: UserSessionInterface): PlayBroadcastAnalytic {
        return PlayBroadcastAnalytic(userSession)
    }

}