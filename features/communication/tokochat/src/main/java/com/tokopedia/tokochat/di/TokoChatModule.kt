package com.tokopedia.tokochat.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.tokochat.data.repository.TokoChatRepository
import com.tokopedia.tokochat.di.TokoChatNetworkModule.RETROFIT_NAME
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
object TokoChatModule {

    @TokoChatScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @TokoChatScope
    @Provides
    fun provideChatServiceRepository(
        @Named(RETROFIT_NAME) retrofit: Retrofit,
        @ApplicationContext context: Context
    ): TokoChatRepository {
        return TokoChatRepository(retrofit, context)
    }
}
