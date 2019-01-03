package com.tokopedia.topchat.revamp.di

import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.chat_common.data.api.ChatRoomApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
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
    fun provideAnalyticTracker(abstractionRouter: AbstractionRouter): AnalyticTracker {
        return abstractionRouter.analyticTracker
    }
}