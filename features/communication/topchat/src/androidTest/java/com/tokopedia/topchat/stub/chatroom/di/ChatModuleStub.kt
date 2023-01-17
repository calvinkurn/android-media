package com.tokopedia.topchat.stub.chatroom.di

import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.topchat.FakeTopchatCacheManager
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.pojo.imageserver.ChatImageServerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import com.tokopedia.topchat.common.di.qualifier.InboxQualifier
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.common.websocket.DefaultWebSocketParser
import com.tokopedia.topchat.common.websocket.DefaultWebSocketStateHandler
import com.tokopedia.topchat.common.websocket.FakeTopchatWebSocket
import com.tokopedia.topchat.common.websocket.TopchatWebSocket
import com.tokopedia.topchat.common.websocket.WebSocketParser
import com.tokopedia.topchat.common.websocket.WebSocketStateHandler
import com.tokopedia.topchat.common.websocket.WebsocketPayloadGenerator
import com.tokopedia.topchat.stub.common.DefaultWebsocketPayloadFakeGenerator
import com.tokopedia.topchat.stub.common.UserSessionStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module(
        includes = arrayOf(
                ChatNetworkModuleStub::class,
                MediaUploaderModule::class
        )
)
class ChatModuleStub {

    private val NET_READ_TIMEOUT = 60
    private val NET_WRITE_TIMEOUT = 60
    private val NET_CONNECT_TIMEOUT = 60
    private val NET_RETRY = 1

    @ChatScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
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
        return OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY)
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
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter,
                                      userSessionInterface: UserSessionInterface):
            FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSessionInterface)
    }

    @ChatScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSessionInterface: UserSessionInterface):
            TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ChatScope
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context,
                            @InboxQualifier retryPolicy: OkHttpRetryPolicy,
                            errorResponseInterceptor: ErrorResponseInterceptor,
                            chuckInterceptor: ChuckerInterceptor,
                            fingerprintInterceptor: FingerprintInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor):
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

    @ChatScope
    @InboxQualifier
    @Provides
    fun provideChatRetrofit(okHttpClient: OkHttpClient,
                            retrofitBuilder: Retrofit.Builder): Retrofit {
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
    fun provideChatRoomSettingUseCase(graphqlRepository: GraphqlRepository)
            : com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<RoomSettingResponse> {
        return com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository)
    }

    @ChatScope
    @Provides
    internal fun provideTopchatCacheManager(): TopchatCacheManager {
        return FakeTopchatCacheManager()
    }

    @ChatScope
    @Provides
    internal fun provideTopchatSharedPrefs(@TopchatContext context: Context): SharedPreferences {
        return context.getSharedPreferences("topchat_prefs", Context.MODE_PRIVATE)
    }

    @ChatScope
    @Provides
    fun provideChatImageServerUseCase(graphqlRepository: GraphqlRepository)
            : com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ChatImageServerResponse> {
        return com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository)
    }


    @ChatScope
    @Provides
    fun provideWebSocketStateHandler(): WebSocketStateHandler {
        return DefaultWebSocketStateHandler()
    }

    @ChatScope
    @Provides
    fun provideFakeTopChatWebSocket(
        mapper: TopChatRoomGetExistingChatMapper,
        session: UserSessionInterface
    ): FakeTopchatWebSocket {
        return FakeTopchatWebSocket(mapper, session)
    }

    @ChatScope
    @Provides
    fun provideTopChatWebSocket(
        ws: FakeTopchatWebSocket
    ): TopchatWebSocket {
        return ws
    }

    @ChatScope
    @Provides
    fun provideWebSocketParser(): WebSocketParser {
        return DefaultWebSocketParser()
    }

    @ChatScope
    @Provides
    fun provideWebsocketFakePayloadGenerator(
        userSession: UserSessionInterface
    ): DefaultWebsocketPayloadFakeGenerator {
        return DefaultWebsocketPayloadFakeGenerator(userSession)
    }

    @ChatScope
    @Provides
    fun provideWebsocketPayloadGenerator(
        fakeGenerator: DefaultWebsocketPayloadFakeGenerator
    ): WebsocketPayloadGenerator {
        return fakeGenerator
    }
}
