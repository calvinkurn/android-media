package com.tokopedia.topchat.chatlist.di

import android.content.Context
import android.content.res.Resources
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iris.util.Session
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.topchat.common.Constant.NET_CONNECT_TIMEOUT
import com.tokopedia.topchat.common.Constant.NET_READ_TIMEOUT
import com.tokopedia.topchat.common.Constant.NET_RETRY
import com.tokopedia.topchat.common.Constant.NET_WRITE_TIMEOUT
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.common.websocket.DefaultTopChatWebSocket
import com.tokopedia.topchat.common.websocket.DefaultTopChatWebSocket.Companion.PAGE_CHATLIST
import com.tokopedia.topchat.common.websocket.DefaultWebSocketParser
import com.tokopedia.topchat.common.websocket.DefaultWebSocketStateHandler
import com.tokopedia.topchat.common.websocket.TopchatWebSocket
import com.tokopedia.topchat.common.websocket.WebSocketParser
import com.tokopedia.topchat.common.websocket.WebSocketStateHandler
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.DEFAULT_PING
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module
class ChatListNetworkModule {

    @ChatListScope
    @Provides
    fun provideWebSocketParser(): WebSocketParser {
        return DefaultWebSocketParser()
    }

    @ChatListScope
    @Provides
    fun provideWebSocketStateHandler(): WebSocketStateHandler {
        return DefaultWebSocketStateHandler()
    }

    @ChatListScope
    @Provides
    fun provideChatRetrofit(
        @ApplicationContext context: Context,
        userSession: UserSession
    ): Retrofit {
        if ((context is NetworkRouter).not()) {
            throw IllegalStateException(
                "Application must implement "
                    .plus(NetworkRouter::class.java.simpleName)
            )
        }

        return CommonNetwork.createRetrofit(
            context,
            ChatUrl.TOPCHAT,
            context as NetworkRouter,
            userSession
        )
    }

    @ChatListScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ChatListScope
    @Provides
    fun provideResources(@TopchatContext context: Context): Resources {
        return context.resources
    }

    @ChatListScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return (context as NetworkRouter)
    }

    @ChatListScope
    @Provides
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(
            NET_READ_TIMEOUT,
            NET_WRITE_TIMEOUT,
            NET_CONNECT_TIMEOUT,
            NET_RETRY
        )
    }

    @ChatListScope
    @Provides
    fun provideResponseInterceptor(): ErrorResponseInterceptor {
        return HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java)
    }

    @ChatListScope
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @ChatListScope
    @Provides
    fun provideFingerprintInterceptor(
        networkRouter: NetworkRouter,
        userSessionInterface: UserSessionInterface
    ):
        FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSessionInterface)
    }

    @ChatListScope
    @Provides
    fun provideTkpdAuthInterceptor(
        @ApplicationContext context: Context,
        networkRouter: NetworkRouter,
        userSessionInterface: UserSessionInterface
    ):
        TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ChatListScope
    @Provides
    fun provideOkHttpClient(
        retryPolicy: OkHttpRetryPolicy,
        errorResponseInterceptor: ErrorResponseInterceptor,
        chuckInterceptor: ChuckerInterceptor,
        fingerprintInterceptor: FingerprintInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ):
        OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(fingerprintInterceptor)
            .addInterceptor(errorResponseInterceptor)
            .pingInterval(DEFAULT_PING, TimeUnit.MILLISECONDS)
            .connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                .addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @ChatListScope
    @Provides
    fun provideTopChatWebSocket(
        userSession: UserSessionInterface,
        client: OkHttpClient,
        irisSession: Session
    ): TopchatWebSocket {
        val webSocketUrl = ChatUrl.CHAT_WEBSOCKET_DOMAIN + ChatUrl.CONNECT_WEBSOCKET +
            "?os_type=1" +
            "&device_id=" + userSession.deviceId +
            "&user_id=" + userSession.userId
        return DefaultTopChatWebSocket(
            client,
            webSocketUrl,
            userSession.accessToken,
            PAGE_CHATLIST,
            irisSession
        )
    }

    @ChatListScope
    @Provides
    fun provideAbTestPlatform(): AbTestPlatform {
        return RemoteConfigInstance.getInstance().abTestPlatform
    }
}
