package com.tokopedia.topchat.chatroom.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.chat_common.network.ChatUrl.Companion.TOPCHAT
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
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

    @ChatScope
    @Provides
    @Named("retrofit")
     fun provideChatRetrofit(@ApplicationContext context: Context, userSession: UserSession): Retrofit {
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


    @ChatScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

    @ChatScope
    @Provides
     fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }


}