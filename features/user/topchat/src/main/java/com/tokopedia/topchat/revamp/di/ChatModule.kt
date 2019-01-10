package com.tokopedia.topchat.revamp.di

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.topchat.chatlist.data.factory.MessageFactory
import com.tokopedia.topchat.chatlist.data.mapper.DeleteMessageMapper
import com.tokopedia.topchat.chatlist.data.mapper.GetMessageMapper
import com.tokopedia.topchat.chatlist.data.repository.MessageRepository
import com.tokopedia.topchat.chatlist.data.repository.MessageRepositoryImpl
import com.tokopedia.topchat.chatlist.data.repository.SendMessageSource
import com.tokopedia.topchat.chatroom.data.mapper.SendMessageMapper
import com.tokopedia.topchat.common.chat.api.ChatApi
import com.tokopedia.topchat.common.di.qualifier.InboxQualifier
import com.tokopedia.topchat.revamp.data.api.ChatRoomApi
import com.tokopedia.topchat.revamp.domain.mapper.GetTemplateChatRoomMapper
import com.tokopedia.topchat.revamp.domain.pojo.TopChatImageUploadPojo
import com.tokopedia.topchat.revamp.domain.usecase.GetTemplateChatRoomUseCase
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

    @ChatScope
    @Provides
    fun provideAnalyticTracker(@ApplicationContext context: Context): AnalyticTracker {
        return (context as AbstractionRouter).analyticTracker
    }

    @ChatScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ChatScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return (context as NetworkRouter)
    }

    @ChatScope
    @Provides
    fun provideResource(@ApplicationContext context: Context): Resources {
        return context.resources
    }


    @Provides
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: com.tokopedia.abstraction.common.data.model.session.UserSession,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<TopChatImageUploadPojo> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession, TopChatImageUploadPojo::class.java, imageUploaderUtils)
    }



    @ChatScope
    @Provides
    internal fun provideChatRoomApi(@Named("retrofit") retrofit: Retrofit): ChatRoomApi {
        return retrofit.create(ChatRoomApi::class.java)
    }

    @ChatScope
    @Provides
    internal fun provideGetTemplateChatUseCase(api: ChatRoomApi, mapper: GetTemplateChatRoomMapper): GetTemplateChatRoomUseCase {
        return GetTemplateChatRoomUseCase(api, mapper)
    }

    @ChatScope
    @Provides
    fun provideAnalyticTracker(abstractionRouter: AbstractionRouter): AnalyticTracker {
        return abstractionRouter.analyticTracker
    }


    @ChatScope
    @Provides
    fun provideResponseInterceptor(): ErrorResponseInterceptor {
        return HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java)
    }

    @ChatScope
    @Provides
    internal fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context)
    }

    @ChatScope
    @Provides
    internal fun provideOkHttpClient(@ApplicationContext context: Context,
                                     @InboxQualifier retryPolicy: OkHttpRetryPolicy,
                                     errorResponseInterceptor: ErrorResponseInterceptor,
                                     chuckInterceptor: ChuckInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor,
                                     networkRouter : NetworkRouter,
                                     userSessionInterface: UserSessionInterface): 
            OkHttpClient {
        val builder = OkHttpClient.Builder()
                .addInterceptor(FingerprintInterceptor(networkRouter, userSessionInterface))
                .addInterceptor(CacheApiInterceptor())
//                .addInterceptor(DigitalHmacAuthInterceptor(AuthUtil.KEY.KEY_WSV4))
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
    internal fun provideChatRetrofit(okHttpClient: OkHttpClient,
                                     retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ChatUrl.TOPCHAT)
                .client(okHttpClient)
                .build()
    }

    @ChatScope
    @Provides
    internal fun provideChatApi(@InboxQualifier retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }


    @ChatScope
    @Provides
    internal fun provideMessageFactory(
            chatApi: ChatApi,
            getMessageMapper: GetMessageMapper,
            deleteMessageMapper: DeleteMessageMapper): MessageFactory {
        return MessageFactory(chatApi, getMessageMapper, deleteMessageMapper)
    }

    @ChatScope
    @Provides
    internal fun provideSendMessageSource(chatApi: ChatApi,
                                          sendMessageMapper: SendMessageMapper): SendMessageSource {
        return SendMessageSource(chatApi, sendMessageMapper)
    }

    @ChatScope
    @Provides
    internal fun provideMessageRepository(messageFactory: MessageFactory,
                                          sendMessageSource: SendMessageSource): MessageRepository {
        return MessageRepositoryImpl(messageFactory, sendMessageSource)
    }

}