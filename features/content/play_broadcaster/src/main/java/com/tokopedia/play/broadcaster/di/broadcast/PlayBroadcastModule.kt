package com.tokopedia.play.broadcaster.di.broadcast

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.pusher.PlayPusher
import com.tokopedia.play.broadcaster.pusher.PlayPusherImpl
import com.tokopedia.play.broadcaster.socket.PlayBroadcastSocket
import com.tokopedia.play.broadcaster.socket.PlayBroadcastSocket.Companion.KEY_GROUP_CHAT_PREFERENCES
import com.tokopedia.play.broadcaster.socket.PlayBroadcastSocketImpl
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play_common.util.coroutine.DefaultCoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by jegul on 20/05/20
 */
@Module
class PlayBroadcastModule(val mContext: Context) {

    @PlayBroadcastScope
    @Provides
    fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider = DefaultCoroutineDispatcherProvider()

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
        return PlayPusherImpl(context)
    }

    @Provides
    fun providePlaySocket(userSession: UserSessionInterface, cacheHandler: LocalCacheHandler): PlayBroadcastSocket {
        return PlayBroadcastSocketImpl(userSession, cacheHandler)
    }

    @Provides
    fun provideGraphQLRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideUpdateChannelUseCase(graphqlRepository: GraphqlRepository): UpdateChannelUseCase {
        return UpdateChannelUseCase(graphqlRepository)
    }

    @PlayBroadcastScope
    @Provides
    fun providePlayBroadcastAnalytic(userSession: UserSessionInterface): PlayBroadcastAnalytic {
        return PlayBroadcastAnalytic(userSession)
    }

    @PlayBroadcastScope
    @Provides
    fun providePlayBroadcastMapper(): PlayBroadcastMapper {
        return PlayBroadcastUiMapper()

        /**
         * If you want configurable
         */
//        return PlayBroadcastConfigurableMapper(PlayBroadcastUiMapper(), PlayBroadcastMockMapper())

        /**
         * If you want mock
         */
//        return PlayBroadcastMockMapper()
    }

}