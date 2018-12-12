package com.tokopedia.topchat.revamp.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.chat_common.network.CHATBOT_BASE_URL
import com.tokopedia.chat_common.network.TOPCHAT
import com.tokopedia.chat_common.network.TOPCHAT_BASE_URL
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

/**
 * @author : Steven 29/11/18
 */

@Module
class ChatNetworkModule {

    @TopChatRoomScope
    @Provides
    @Named("retrofit")
    internal fun provideChatRetrofit(@ApplicationContext context: Context, userSession: UserSession): Retrofit {
        if ((context is NetworkRouter).not()) {
            throw IllegalStateException("Application must implement "
                    .plus(NetworkRouter::class.java.simpleName)
            )
        }

        return CommonNetwork.createRetrofit(
                context,
                TOPCHAT,
                context as NetworkRouter,
                userSession
        )
    }

    @TopChatRoomScope
    @Provides
    internal fun provideChatBotRetrofit(@ApplicationContext context: Context, userSession: UserSession): Retrofit {
        if ((context is NetworkRouter).not()) {
            throw IllegalStateException("Application must implement "
                    .plus(NetworkRouter::class.java.simpleName)
            )
        }

        return CommonNetwork.createRetrofit(
                context,
                CHATBOT_BASE_URL,
                context as NetworkRouter,
                userSession
        )
    }

    @TopChatRoomScope
    @Provides
    internal fun provideChatRetrofitJsDomain(@ApplicationContext context: Context, userSession: UserSession): Retrofit {
        if ((context is NetworkRouter).not()) {
            throw IllegalStateException("Application must implement "
                    .plus(NetworkRouter::class.java.simpleName)
            )
        }

        return CommonNetwork.createRetrofit(
                context,
                TOPCHAT_BASE_URL,
                context as NetworkRouter,
                userSession
        )
    }

    @TopChatRoomScope
    @Provides
    internal fun provideWsRetrofitDomain(@ApplicationContext context: Context, userSession: UserSession): Retrofit {
        if ((context is NetworkRouter).not()) {
            throw IllegalStateException("Application must implement "
                    .plus(NetworkRouter::class.java.simpleName)
            )
        }

        return CommonNetwork.createRetrofit(
                context,
                TkpdBaseURL.BASE_DOMAIN,
                context as NetworkRouter,
                userSession
        )
    }

    @TopChatRoomScope
    @Provides
    internal fun provideTomeRetrofitDomain(@ApplicationContext context: Context, userSession: UserSession): Retrofit {
        if ((context is NetworkRouter).not()) {
            throw IllegalStateException("Application must implement "
                    .plus(NetworkRouter::class.java.simpleName)
            )
        }

        return CommonNetwork.createRetrofit(
                context,
                TkpdBaseURL.TOME_DOMAIN,
                context as NetworkRouter,
                userSession
        )
    }


    @TopChatRoomScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }
}