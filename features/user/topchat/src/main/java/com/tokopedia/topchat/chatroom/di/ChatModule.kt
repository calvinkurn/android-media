package com.tokopedia.topchat.chatroom.di

import android.content.Context
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.topchat.chatlist.data.factory.MessageFactory
import com.tokopedia.topchat.chatlist.data.mapper.DeleteMessageMapper
import com.tokopedia.topchat.chatlist.data.mapper.GetMessageMapper
import com.tokopedia.topchat.chatlist.data.repository.MessageRepository
import com.tokopedia.topchat.chatlist.data.repository.MessageRepositoryImpl
import com.tokopedia.topchat.chatroom.view.listener.ChatSettingsInterface
import com.tokopedia.topchat.chatroom.view.presenter.ChatSettingsPresenter
import com.tokopedia.topchat.common.analytics.ChatSettingsAnalytics
import com.tokopedia.topchat.common.chat.api.ChatApi
import com.tokopedia.topchat.common.di.qualifier.InboxQualifier
import com.tokopedia.topchat.common.network.XUserIdInterceptor
import com.tokopedia.topchat.chatroom.data.api.ChatRoomApi
import com.tokopedia.topchat.chatroom.domain.mapper.GetTemplateChatRoomMapper
import com.tokopedia.topchat.chatroom.domain.pojo.TopChatImageUploadPojo
import com.tokopedia.topchat.chatroom.domain.usecase.GetTemplateChatRoomUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
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

@Module(includes = arrayOf(ImageUploaderModule::class, ChatNetworkModule::class))
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

    @Provides
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: UserSessionInterface,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<TopChatImageUploadPojo> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession, TopChatImageUploadPojo::class.java, imageUploaderUtils)
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
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context)
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
    fun provideOkHttpClient(@InboxQualifier retryPolicy: OkHttpRetryPolicy,
                            errorResponseInterceptor: ErrorResponseInterceptor,
                            chuckInterceptor: ChuckInterceptor,
                            fingerprintInterceptor: FingerprintInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            xUserIdInterceptor: XUserIdInterceptor):
            OkHttpClient {
        val builder = OkHttpClient.Builder()
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(CacheApiInterceptor())
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
            getMessageMapper: GetMessageMapper,
            deleteMessageMapper: DeleteMessageMapper): MessageFactory {
        return MessageFactory(chatApi, getMessageMapper, deleteMessageMapper)
    }

    @ChatScope
    @Provides
    fun provideMessageRepository(messageFactory: MessageFactory): MessageRepository {
        return MessageRepositoryImpl(messageFactory)
    }

    @ChatScope
    @Provides
    fun provideChatSettingsPresenter(graphqlUseCase: GraphqlUseCase,
                                     chatSettingsAnalytics: ChatSettingsAnalytics):
            ChatSettingsInterface.Presenter {
        return ChatSettingsPresenter(graphqlUseCase, chatSettingsAnalytics)
    }

}