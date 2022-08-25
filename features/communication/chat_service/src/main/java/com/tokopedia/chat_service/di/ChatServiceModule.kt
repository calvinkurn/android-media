package com.tokopedia.chat_service.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.chat_service.data.repository.ChatServiceRepository
import com.tokopedia.chat_service.di.ChatServiceNetworkModule.RETROFIT_NAME
import com.tokopedia.network.NetworkRouter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
object ChatServiceModule {

    @ChatServiceScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ChatServiceScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return (context as NetworkRouter)
    }

    @ChatServiceScope
    @Provides
    fun provideChatServiceRepository(
        @Named(RETROFIT_NAME) retrofit: Retrofit,
        @ApplicationContext context: Context
    ): ChatServiceRepository {
        return ChatServiceRepository(retrofit, context)
    }
}