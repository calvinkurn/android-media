package com.tokopedia.topchat.chatroom.di

import android.content.Context
import android.content.res.Resources
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
class ChatNetworkModule(val context: Context) {

    @Provides
    fun provideContext(): Context = context

    @ChatScope
    @Provides
    @Named("retrofit")
    fun provideChatRetrofit(context: Context, userSession: UserSession): Retrofit {
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
    fun provideUserSession(context: Context): UserSession {
        return UserSession(context)
    }

    @ChatScope
    @Provides
    fun provideResources(context: Context): Resources {
        return context.resources
    }


}