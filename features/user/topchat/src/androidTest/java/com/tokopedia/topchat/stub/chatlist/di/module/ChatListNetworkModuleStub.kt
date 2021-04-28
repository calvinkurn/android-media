package com.tokopedia.topchat.stub.chatlist.di.module

import android.content.Context
import android.content.res.Resources
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.topchat.chatlist.di.ChatListScope
import com.tokopedia.topchat.chatlist.domain.websocket.DefaultTopChatWebSocket
import com.tokopedia.topchat.chatlist.domain.websocket.DefaultWebSocketParser
import com.tokopedia.topchat.chatlist.domain.websocket.WebSocketParser
import com.tokopedia.topchat.common.chat.api.ChatApi
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.common.network.XUserIdInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class ChatListNetworkModuleStub(
        private val userSessionInterface: UserSessionInterface
) {

    private val NET_READ_TIMEOUT = 60
    private val NET_WRITE_TIMEOUT = 60
    private val NET_CONNECT_TIMEOUT = 60
    private val NET_RETRY = 1

    @ChatListScope
    @Provides
    fun provideChatRetrofit(@ApplicationContext context: Context, userSession: UserSession): Retrofit {
        return CommonNetwork.createRetrofit(
                context,
                ChatUrl.TOPCHAT,
                context as NetworkRouter,
                userSession
        )
    }

    @ChatListScope
    @Provides
    fun provideWebSocketParser(): WebSocketParser {
        return DefaultWebSocketParser()
    }

    @ChatListScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return userSessionInterface
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
        return OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY)
    }


    @ChatListScope
    @Provides
    fun provideChatApi(@Named("retrofit") retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
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
    fun provideXUserIdInterceptor(@ApplicationContext context: Context,
                                  networkRouter: NetworkRouter,
                                  userSession: UserSessionInterface):
            XUserIdInterceptor {
        return XUserIdInterceptor(context, networkRouter, userSession)
    }

    @ChatListScope
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter,
                                      userSessionInterface: UserSessionInterface):
            FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSessionInterface)
    }

    @ChatListScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSessionInterface: UserSessionInterface):
            TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ChatListScope
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context,
                            retryPolicy: OkHttpRetryPolicy,
                            errorResponseInterceptor: ErrorResponseInterceptor,
                            chuckInterceptor: ChuckerInterceptor,
                            fingerprintInterceptor: FingerprintInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            xUserIdInterceptor: XUserIdInterceptor):
            OkHttpClient {
        val builder = OkHttpClient.Builder()
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(xUserIdInterceptor)
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

    // TODO: switch to fake
    @ChatListScope
    @Provides
    fun provideTopChatWebSocket(
            userSession: UserSessionInterface,
            client: OkHttpClient
    ): DefaultTopChatWebSocket {
        val webSocketUrl = ChatUrl.CHAT_WEBSOCKET_DOMAIN + ChatUrl.CONNECT_WEBSOCKET +
                "?os_type=1" +
                "&device_id=" + userSession.deviceId +
                "&user_id=" + userSession.userId
        return DefaultTopChatWebSocket(client, webSocketUrl, userSession.accessToken)
    }
}