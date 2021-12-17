package com.tokopedia.play.broadcaster.di.broadcast

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.broadcaster.LiveBroadcaster
import com.tokopedia.broadcaster.LiveBroadcasterManager
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalytic
import com.tokopedia.play.broadcaster.analytic.tag.PlayBroadcastContentTaggingAnalytic
import com.tokopedia.play.broadcaster.pusher.PlayLivePusher
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherImpl
import com.tokopedia.play.broadcaster.pusher.mediator.LiveBroadcasterMediator
import com.tokopedia.play.broadcaster.pusher.mediator.PlayLivePusherMediator
import com.tokopedia.play.broadcaster.pusher.mediator.PusherMediator
import com.tokopedia.play.broadcaster.pusher.mediator.rollence.AbTestBroadcaster
import com.tokopedia.play.broadcaster.pusher.timer.PlayLivePusherTimer
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.play_common.websocket.KEY_GROUP_CHAT_PREFERENCES
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.PlayWebSocketImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

/**
 * Created by jegul on 20/05/20
 */
@Module
class PlayBroadcastModule(private val mContext: Context) {

    @Provides
    fun provideContext(): Context {
        return mContext
    }

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideLocalCacheHandler(): LocalCacheHandler {
        return LocalCacheHandler(mContext, KEY_GROUP_CHAT_PREFERENCES)
    }

    @Provides
    fun providePlayLivePusherMediator(localCacheHandler: LocalCacheHandler, playLivePusherTimer: PlayLivePusherTimer): PusherMediator {
        return if (AbTestBroadcaster.isUseBroadcasterSdk()) {
            LiveBroadcasterMediator(LiveBroadcasterManager(), localCacheHandler, playLivePusherTimer)
        } else {
            PlayLivePusherMediator(PlayLivePusherImpl(), localCacheHandler, playLivePusherTimer)
        }
    }

    @PlayBroadcastScope
    @Provides
    fun provideWebSocket(userSession: UserSessionInterface, dispatchers: CoroutineDispatchers): PlayWebSocket {
        return PlayWebSocketImpl(
            OkHttpClient.Builder(),
            userSession,
            dispatchers
        )
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
    fun providePlayBroadcastAnalytic(
        userSession: UserSessionInterface,
        contentTaggingAnalytic: PlayBroadcastContentTaggingAnalytic,
        interactiveAnalytic: PlayBroadcastInteractiveAnalytic,
    ): PlayBroadcastAnalytic {
        return PlayBroadcastAnalytic(userSession, contentTaggingAnalytic, interactiveAnalytic)
    }

    @PlayBroadcastScope
    @Provides
    fun provideHtmlTextTransformer(): HtmlTextTransformer {
        return DefaultHtmlTextTransformer()
    }

    @PlayBroadcastScope
    @Provides
    fun providePlayBroadcastMapper(htmlTextTransformer: HtmlTextTransformer): PlayBroadcastMapper {
        return PlayBroadcastUiMapper(htmlTextTransformer)

        /**
         * If you want mock
         */
//        return PlayBroadcastMockMapper()
    }
}