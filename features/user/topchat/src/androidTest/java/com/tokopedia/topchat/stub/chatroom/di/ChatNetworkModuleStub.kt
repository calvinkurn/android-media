package com.tokopedia.topchat.stub.chatroom.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
class ChatNetworkModuleStub {
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
                ChatUrl.TOPCHAT,
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
    fun provideResources(@TopchatContext context: Context): Resources {
        return context.resources
    }
}