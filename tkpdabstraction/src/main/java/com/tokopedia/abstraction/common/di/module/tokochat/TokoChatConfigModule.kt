package com.tokopedia.abstraction.common.di.module.tokochat

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.tokochat.tokochat_config_common.dagger.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object TokoChatConfigModule {

    @Provides
    @TokoChatQualifier
    fun provideTokoChatContext(
        @ApplicationContext context: Context
    ): Context {
        return context
    }

    @Provides
    @TokoChatQualifier
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @TokoChatQualifier
    fun providesRemoteConfig(
        @ApplicationContext context: Context
    ): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    @TokoChatQualifier
    fun provideTokoChatRepository(
        @TokoChatQualifier retrofit: Retrofit,
        @ApplicationContext context: Context,
        babbleCourierClient: BabbleCourierClient
    ): TokoChatRepository {
        return TokoChatRepository(retrofit, context, babbleCourierClient)
    }
}
