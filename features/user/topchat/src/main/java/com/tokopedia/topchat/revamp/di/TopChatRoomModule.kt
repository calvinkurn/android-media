package com.tokopedia.topchat.revamp.di

import android.content.Context
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
import com.tokopedia.core.network.di.qualifier.InboxQualifier
import com.tokopedia.core.network.retrofit.interceptors.DigitalHmacAuthInterceptor
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor
import com.tokopedia.core.network.retrofit.utils.AuthUtil
import com.tokopedia.topchat.chatlist.data.factory.MessageFactory
import com.tokopedia.topchat.chatlist.data.mapper.DeleteMessageMapper
import com.tokopedia.topchat.chatlist.data.mapper.GetMessageMapper
import com.tokopedia.topchat.chatlist.data.repository.MessageRepository
import com.tokopedia.topchat.chatlist.data.repository.MessageRepositoryImpl
import com.tokopedia.topchat.chatlist.data.repository.SendMessageSource
import com.tokopedia.topchat.common.chat.api.ChatApi
import com.tokopedia.topchat.common.di.InboxChatScope
import com.tokopedia.topchat.revamp.data.api.ChatRoomApi
import com.tokopedia.topchat.revamp.domain.mapper.GetTemplateChatRoomMapper
import com.tokopedia.topchat.revamp.domain.usecase.GetTemplateChatRoomUseCase
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


@Module(includes = [(ChatNetworkModule::class)])
class TopChatRoomModule {
//
//
//    @InboxChatRoomScope
//    @Provides
//    fun provideShopCommonWsApi(@RetrofitWsDomainQualifier retrofit: Retrofit): ShopCommonWSApi {
//        return retrofit.create(ShopCommonWSApi::class.java!!)
//    }
//
//    @InboxChatRoomScope
//    @Provides
//    fun provideShopCommonApi(@RetrofitTomeDomainQualifier retrofit: Retrofit): ShopCommonApi {
//        return retrofit.create(ShopCommonApi::class.java!!)
//    }
//
//
//    @TopChatRoomScope
//    @Provides
//    internal fun provideTopChatApi(@RetrofitJsDomainQualifier retrofit: Retrofit): TopChatApi {
//        return retrofit.create(TopChatApi::class.java)
//    }

    @TopChatRoomScope
    @Provides
    internal fun provideChatRoomApi(@Named("retrofit") retrofit: Retrofit): ChatRoomApi {
        return retrofit.create(ChatRoomApi::class.java)
    }

    @TopChatRoomScope
    @Provides
    internal fun provideGetTemplateChatUseCase(api: ChatRoomApi, mapper: GetTemplateChatRoomMapper): GetTemplateChatRoomUseCase {
        return GetTemplateChatRoomUseCase(api, mapper)
    }

    @TopChatRoomScope
    @Provides
    fun provideAnalyticTracker(abstractionRouter: AbstractionRouter): AnalyticTracker {
        return abstractionRouter.analyticTracker
    }


    @TopChatRoomScope
    @Provides
    fun provideResponseInterceptor(): ErrorResponseInterceptor {
        return HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java)
    }

    @TopChatRoomScope
    @Provides
    internal fun provideOkHttpClient(@ApplicationContext context: Context,
                                     @InboxQualifier retryPolicy: OkHttpRetryPolicy,
                                     errorResponseInterceptor: ErrorResponseInterceptor,
                                     chuckInterceptor: ChuckInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .addInterceptor(FingerprintInterceptor(context))
                .addInterceptor(CacheApiInterceptor())
                .addInterceptor(DigitalHmacAuthInterceptor(AuthUtil.KEY.KEY_WSV4))
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


    @TopChatRoomScope
    @Provides
    internal fun provideChatApi(@InboxQualifier retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }


    @InboxChatScope
    @Provides
    internal fun provideMessageFactory(
            chatApi: ChatApi,
            getMessageMapper: GetMessageMapper,
            deleteMessageMapper: DeleteMessageMapper): MessageFactory {
        return MessageFactory(chatApi, getMessageMapper, deleteMessageMapper)
    }

    @TopChatRoomScope
    @Provides
    internal fun provideMessageRepository(messageFactory: MessageFactory,
                                          sendMessageSource: SendMessageSource): MessageRepository {
        return MessageRepositoryImpl(messageFactory, sendMessageSource)
    }

}