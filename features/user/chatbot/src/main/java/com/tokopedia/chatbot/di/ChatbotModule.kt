package com.tokopedia.chatbot.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.chatbot.data.cache.ChatbotCacheManager
import com.tokopedia.chatbot.data.cache.ChatbotCacheManagerImpl
import com.tokopedia.chatbot.util.GetUserNameForReplyBubble
import com.tokopedia.chatbot.websocket.ChatbotDefaultWebSocketStateHandler
import com.tokopedia.chatbot.websocket.ChatbotWebSocket
import com.tokopedia.chatbot.websocket.ChatbotWebSocketImpl
import com.tokopedia.chatbot.websocket.ChatbotWebSocketStateHandler
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author by nisie on 12/12/18.
 */
@Module
class ChatbotModule {

    constructor(context: Context) {
        thisContext = context
    }

    private var thisContext: Context

    @ChatbotScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ChatbotScope
    @Provides
    fun provideResource(): Resources {
        return thisContext.resources
    }

    @ChatbotScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @ChatbotScope
    @Provides
    fun provideChatTkpdAuthInterceptor(
        @ApplicationContext context: Context,
        networkRouter: NetworkRouter,
        userSessionInterface: UserSessionInterface
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ChatbotScope
    @Provides
    fun provideFingerprintInterceptor(
        networkRouter: NetworkRouter,
        userSession: UserSessionInterface
    ): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @ChatbotScope
    @Provides
    fun provideBaseRepository(): BaseRepository {
        return BaseRepository()
    }

    @Provides
    internal fun provideChatbotCacheManager(@ApplicationContext context: Context): ChatbotCacheManager {
        val chatbotCacheManager = context.getSharedPreferences("chatbotCache", Context.MODE_PRIVATE)
        return ChatbotCacheManagerImpl(chatbotCacheManager)
    }

    @ChatbotScope
    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @ChatbotScope
    @Provides
    fun provideGetUserNameForReplyBubble(userSession: UserSessionInterface): GetUserNameForReplyBubble {
        return GetUserNameForReplyBubble(userSession)
    }

    @ChatbotScope
    @Provides
    fun provideChatbotWebSocket(
        tkpdAuthInterceptor: TkpdAuthInterceptor,
        fingerprintInterceptor: FingerprintInterceptor,
        userSession: UserSessionInterface,
        dispatcher: CoroutineDispatchers
    ): ChatbotWebSocket {
        return ChatbotWebSocketImpl(
            arrayListOf(tkpdAuthInterceptor, fingerprintInterceptor),
            userSession.accessToken,
            dispatcher
        )
    }

    @ChatbotScope
    @Provides
    fun provideChatbotWebSocketStateHandler(): ChatbotWebSocketStateHandler {
        return ChatbotDefaultWebSocketStateHandler()
    }
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository
}
