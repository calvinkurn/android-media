package com.tokopedia.chat_common.di

import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.chat_common.data.api.ChatRoomApi
import com.tokopedia.chat_common.domain.GetChatUseCase
import com.tokopedia.chat_common.domain.mapper.GetChatMapper
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

/**
 * @author : Steven 29/11/18
 */


@Module(includes = [(ChatNetworkModule::class)])
class ChatRoomModule {
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
//    @ChatRoomScope
//    @Provides
//    internal fun provideTopChatApi(@RetrofitJsDomainQualifier retrofit: Retrofit): TopChatApi {
//        return retrofit.create(TopChatApi::class.java)
//    }

    @ChatRoomScope
    @Provides
    internal fun provideChatRoomApi(@Named("retrofit") retrofit: Retrofit): ChatRoomApi {
        return retrofit.create(ChatRoomApi::class.java)
    }

    @ChatRoomScope
    @Provides
    internal fun provideGetChatUseCase(api: ChatRoomApi, mapper: GetChatMapper): GetChatUseCase {
        return GetChatUseCase(api, mapper)
    }

    @ChatRoomScope
    @Provides
    fun provideAnalyticTracker(abstractionRouter: AbstractionRouter): AnalyticTracker {
        return abstractionRouter.analyticTracker
    }
}