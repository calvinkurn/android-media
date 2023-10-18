package com.tokopedia.topchat.stub.chatlist.di.module

import android.content.Context
import android.content.res.Resources
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iris.util.Session
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.common.websocket.DefaultTopChatWebSocket
import com.tokopedia.topchat.common.websocket.DefaultWebSocketParser
import com.tokopedia.topchat.common.websocket.DefaultWebSocketStateHandler
import com.tokopedia.topchat.common.websocket.TopchatWebSocket
import com.tokopedia.topchat.common.websocket.WebSocketParser
import com.tokopedia.topchat.common.websocket.WebSocketStateHandler
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
class ChatListNetworkModuleStub {

    private val NET_READ_TIMEOUT = 60
    private val NET_WRITE_TIMEOUT = 60
    private val NET_CONNECT_TIMEOUT = 60
    private val NET_RETRY = 1

    @ActivityScope
    @Provides
    fun provideWebSocketParser(): WebSocketParser {
        return DefaultWebSocketParser()
    }

    @ActivityScope
    @Provides
    fun provideWebSocketStateHandler(): WebSocketStateHandler {
        return DefaultWebSocketStateHandler()
    }

    @ActivityScope
    @Provides
    fun provideResources(@TopchatContext context: Context): Resources {
        return context.resources
    }

    @ActivityScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return (context as NetworkRouter)
    }

    @ActivityScope
    @Provides
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(
            NET_READ_TIMEOUT,
            NET_WRITE_TIMEOUT,
            NET_CONNECT_TIMEOUT,
            NET_RETRY
        )
    }

    @ActivityScope
    @Provides
    fun provideResponseInterceptor(): ErrorResponseInterceptor {
        return HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java)
    }

    @ActivityScope
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @ActivityScope
    @Provides
    fun provideFingerprintInterceptor(
        networkRouter: NetworkRouter,
        userSessionInterface: UserSessionInterface
    ):
        FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSessionInterface)
    }

    @ActivityScope
    @Provides
    fun provideTkpdAuthInterceptor(
        @ApplicationContext context: Context,
        networkRouter: NetworkRouter,
        userSessionInterface: UserSessionInterface
    ):
        TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ActivityScope
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
            .connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                .addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    // Need to switch to fake later
    @ActivityScope
    @Provides
    fun provideTopChatWebSocket(
        @ApplicationContext context: Context,
        userSession: UserSessionInterface,
        client: OkHttpClient,
        irisSession: Session,
        parser: WebSocketParser
    ): TopchatWebSocket {
        val webSocketUrl = ChatUrl.CHAT_WEBSOCKET_DOMAIN + ChatUrl.CONNECT_WEBSOCKET +
            "?os_type=1" +
            "&device_id=" + userSession.deviceId +
            "&user_id=" + userSession.userId
        return DefaultTopChatWebSocket(
            context,
            client,
            webSocketUrl,
            userSession.accessToken,
            "chatlist",
            irisSession,
            parser
        )
    }

    @ActivityScope
    @Provides
    fun provideAbTestPlatform(): AbTestPlatform {
        return RemoteConfigInstance.getInstance().abTestPlatform
    }
}
