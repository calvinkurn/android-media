package com.tokopedia.play.broadcaster.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.broadcaster.data.socket.PlayBroadcastSocket.Companion.KEY_GROUPCHAT_PREFERENCES
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.pusher.PlayPusher
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * Created by jegul on 20/05/20
 */
@Module
class PlayBroadcasterModule(val mContext: Context) {

    @Provides
    @Named(PlayBroadcastDispatcher.MAIN)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Named(PlayBroadcastDispatcher.IO)
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Named(PlayBroadcastDispatcher.COMPUTATION)
    fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideLocalCacheHandler(): LocalCacheHandler {
        return LocalCacheHandler(mContext, KEY_GROUPCHAT_PREFERENCES)
    }

    @Provides
    fun providePlayPusher(@ApplicationContext context: Context): PlayPusher {
        return PlayPusher.Builder(context).build()
    }

    @Provides
    fun provideGraphQLRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}