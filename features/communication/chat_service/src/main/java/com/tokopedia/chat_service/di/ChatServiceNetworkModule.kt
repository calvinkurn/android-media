package com.tokopedia.chat_service.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
object ChatServiceNetworkModule {

    const val RETROFIT_NAME = "retrofit_chatservice"

    @ChatServiceScope
    @Provides
    @Named(RETROFIT_NAME)
    fun provideChatRetrofit(@ApplicationContext context: Context, userSession: UserSession): Retrofit {
        if ((context is NetworkRouter).not()) {
            throw IllegalStateException("Application must implement "
                .plus(NetworkRouter::class.java.simpleName)
            )
        }

        return CommonNetwork.createRetrofit(
            context,
            TokopediaUrl.getInstance().CHAT,
            context as NetworkRouter,
            userSession
        )
    }

    @ChatServiceScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }
}