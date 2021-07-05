package com.tokopedia.topchat.chatroom.di

import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.di.MediaUploaderModule
import com.tokopedia.mediauploader.di.MediaUploaderNetworkModule
import com.tokopedia.mediauploader.di.NetworkModule
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.chatlist.data.factory.MessageFactory
import com.tokopedia.topchat.chatlist.data.mapper.DeleteMessageMapper
import com.tokopedia.topchat.chatlist.data.repository.MessageRepository
import com.tokopedia.topchat.chatlist.data.repository.MessageRepositoryImpl
import com.tokopedia.topchat.chatroom.data.api.ChatRoomApi
import com.tokopedia.topchat.chatroom.domain.mapper.GetTemplateChatRoomMapper
import com.tokopedia.topchat.chatroom.domain.pojo.imageserver.ChatImageServerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import com.tokopedia.topchat.chatroom.domain.usecase.GetTemplateChatRoomUseCase
import com.tokopedia.topchat.common.chat.api.ChatApi
import com.tokopedia.topchat.common.di.qualifier.InboxQualifier
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.common.network.TopchatCacheManagerImpl
import com.tokopedia.topchat.common.network.XUserIdInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
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
                MediaUploaderModule::class,
                MediaUploaderNetworkModule::class,
                NetworkModule::class
        )
)
class ChatModule {

    private val NET_READ_TIMEOUT = 60
    private val NET_WRITE_TIMEOUT = 60
    private val NET_CONNECT_TIMEOUT = 60
    private val NET_RETRY = 1

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
        return OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY)
    }

    @ChatScope
    @Provides
    fun provideChatRoomApi(@Named("retrofit") retrofit: Retrofit): ChatRoomApi {
        return retrofit.create(ChatRoomApi::class.java)
    }

    @ChatScope
    @Provides
    fun provideGetTemplateChatUseCase(api: ChatRoomApi, mapper: GetTemplateChatRoomMapper): GetTemplateChatRoomUseCase {
        return GetTemplateChatRoomUseCase(api, mapper)
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
    fun provideXUserIdInterceptor(@ApplicationContext context: Context,
                                  networkRouter: NetworkRouter,
                                  userSession: UserSession):
            XUserIdInterceptor {
        return XUserIdInterceptor(context, networkRouter, userSession)
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
    fun provideRxWebSocketUtil(
            tkpdAuthInterceptor: TkpdAuthInterceptor,
            fingerprintInterceptor: FingerprintInterceptor
    ): RxWebSocketUtil {
        val interceptors = listOf(tkpdAuthInterceptor, fingerprintInterceptor)
        return RxWebSocketUtil.getInstance(interceptors)
    }

    @ChatScope
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context,
                            @InboxQualifier retryPolicy: OkHttpRetryPolicy,
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
    fun provideChatApi(@InboxQualifier retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }


    @ChatScope
    @Provides
    fun provideMessageFactory(
            chatApi: ChatApi,
            deleteMessageMapper: DeleteMessageMapper): MessageFactory {
        return MessageFactory(chatApi, deleteMessageMapper)
    }

    @ChatScope
    @Provides
    fun provideMessageRepository(messageFactory: MessageFactory): MessageRepository {
        return MessageRepositoryImpl(messageFactory)
    }

    @ChatScope
    @Provides
    @Named("atcMutation")
    fun provideAddToCartMutation(@TopchatContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart)
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
    internal fun provideAddWishListUseCase(@TopchatContext context: Context): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @ChatScope
    @Provides
    internal fun provideTopchatCacheManager(@TopchatContext context: Context): TopchatCacheManager {
        val topchatCachePref = context.getSharedPreferences("topchatCache", Context.MODE_PRIVATE)
        return TopchatCacheManagerImpl(topchatCachePref)
    }

    @ChatScope
    @Provides
    internal fun provideRemoveWishListUseCase(@TopchatContext context: Context): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
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
    fun provideChatReplyUseCase(graphqlRepository: GraphqlRepository)
            : com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ChatReplyPojo> {
        return com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository)
    }

    @ChatScope
    @Provides
    fun provideRemoteConfig(@TopchatContext context: Context) : RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}