package com.tokopedia.topchat.chatroom.di

import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.analyticsdebugger.debugger.WebSocketLogger
import com.tokopedia.analyticsdebugger.debugger.ws.TopchatWebSocketLogger
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.iris.util.Session
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.topchat.chatroom.data.api.ChatRoomApi
import com.tokopedia.topchat.chatroom.domain.pojo.imageserver.ChatImageServerResponse
import com.tokopedia.topchat.common.Constant.NET_CONNECT_TIMEOUT
import com.tokopedia.topchat.common.Constant.NET_READ_TIMEOUT
import com.tokopedia.topchat.common.Constant.NET_RETRY
import com.tokopedia.topchat.common.Constant.NET_WRITE_TIMEOUT
import com.tokopedia.topchat.common.di.qualifier.InboxQualifier
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.common.network.TopchatCacheManagerImpl
import com.tokopedia.topchat.common.websocket.*
import com.tokopedia.topchat.common.websocket.DefaultTopChatWebSocket.Companion.PAGE_CHATROOM
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.DEFAULT_PING
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named

/**
 * @author : Steven 29/11/18
 */

@Module(
    includes = arrayOf(
        ChatNetworkModule::class,
        MediaUploaderModule::class
    )
)
class ChatModule {

    @ChatScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ChatScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return (context as NetworkRouter)
    }

    @ChatScope
    @InboxQualifier
    @Provides
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(
            NET_READ_TIMEOUT,
            NET_WRITE_TIMEOUT,
            NET_CONNECT_TIMEOUT,
            NET_RETRY
        )
    }

    @ChatScope
    @Provides
    fun provideChatRoomApi(@Named("retrofit") retrofit: Retrofit): ChatRoomApi {
        return retrofit.create(ChatRoomApi::class.java)
    }

    @ChatScope
    @Provides
    fun provideResponseInterceptor(): ErrorResponseInterceptor {
        return HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java)
    }

    @ChatScope
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @ChatScope
    @Provides
    fun provideFingerprintInterceptor(
        networkRouter: NetworkRouter,
        userSessionInterface: UserSessionInterface
    ):
        FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSessionInterface)
    }

    @ChatScope
    @Provides
    fun provideTkpdAuthInterceptor(
        @ApplicationContext context: Context,
        networkRouter: NetworkRouter,
        userSessionInterface: UserSessionInterface
    ):
        TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ChatScope
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        @InboxQualifier retryPolicy: OkHttpRetryPolicy,
        errorResponseInterceptor: ErrorResponseInterceptor,
        chuckInterceptor: ChuckerInterceptor,
        fingerprintInterceptor: FingerprintInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ):
        OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(fingerprintInterceptor)
            .addInterceptor(errorResponseInterceptor)
            .connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
            .pingInterval(DEFAULT_PING, TimeUnit.MILLISECONDS)

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                .addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @ChatScope
    @InboxQualifier
    @Provides
    fun provideChatRetrofit(
        okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): Retrofit {
        return retrofitBuilder.baseUrl(ChatUrl.TOPCHAT)
            .client(okHttpClient)
            .build()
    }

    @ChatScope
    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @ChatScope
    @Provides
    internal fun provideAddToWishlistV2UseCase(graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }

    @ChatScope
    @Provides
    internal fun provideTopchatCacheManager(@TopchatContext context: Context): TopchatCacheManager {
        val topchatCachePref = context.getSharedPreferences("topchatCache", Context.MODE_PRIVATE)
        return TopchatCacheManagerImpl(topchatCachePref)
    }

    @ChatScope
    @Provides
    internal fun provideDeleteWishlistV2UseCase(graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase {
        return DeleteWishlistV2UseCase(graphqlRepository)
    }

    @ChatScope
    @Provides
    internal fun provideTopchatSharedPrefs(@TopchatContext context: Context): SharedPreferences {
        return context.getSharedPreferences("topchat_prefs", Context.MODE_PRIVATE)
    }

    @ChatScope
    @Provides
    fun provideChatImageServerUseCase(graphqlRepository: GraphqlRepository): com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ChatImageServerResponse> {
        return com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository)
    }

    @ChatScope
    @Provides
    fun provideChatReplyUseCase(graphqlRepository: GraphqlRepository): com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ChatReplyPojo> {
        return com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository)
    }

    @ChatScope
    @Provides
    fun provideRemoteConfig(@TopchatContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @ChatScope
    @Provides
    fun provideAbTestPlatform(): AbTestPlatform {
        return RemoteConfigInstance.getInstance().abTestPlatform
    }

    @ChatScope
    @Provides
    fun provideWebSocketStateHandler(): WebSocketStateHandler {
        return DefaultWebSocketStateHandler()
    }

    @ChatScope
    @Provides
    fun provideTopChatWebSocket(
        userSession: UserSessionInterface,
        client: OkHttpClient,
        irisSession: Session,
        webSocketParser: WebSocketParser,
        webSocketLogger: WebSocketLogger
    ): TopchatWebSocket {
        val webSocketUrl = ChatUrl.CHAT_WEBSOCKET_DOMAIN
            .plus(ChatUrl.CONNECT_WEBSOCKET)
            .plus("?os_type=1")
            .plus("&device_id=%s")
            .plus("&user_id=%s")
            .format(
                userSession.deviceId,
                userSession.userId
            )
        return DefaultTopChatWebSocket(
            client,
            webSocketUrl,
            userSession.accessToken,
            PAGE_CHATROOM,
            irisSession,
            webSocketParser,
            webSocketLogger
        )
    }

    @Provides
    @ChatScope
    fun provideWebSocketLogger(
        @ApplicationContext context: Context
    ) : WebSocketLogger {
        return if (GlobalConfig.isAllowDebuggingTools()) {
            TopchatWebSocketLogger(context)
        } else {
            object : WebSocketLogger {
                override fun init(data: String) = Unit
                override fun send(event: String, message: String) = Unit
                override fun send(event: String) = Unit
            }
        }
    }

    @ChatScope
    @Provides
    fun provideWebSocketParser(): WebSocketParser {
        return DefaultWebSocketParser()
    }

    @ChatScope
    @Provides
    fun provideWebsocketPayloadGenerator(
        userSession: UserSessionInterface
    ): WebsocketPayloadGenerator {
        return DefaultWebsocketPayloadGenerator(userSession)
    }

    @ChatScope
    @Provides
    fun provideIrisSession(@ApplicationContext context: Context): Session {
        return IrisSession(context)
    }
}
