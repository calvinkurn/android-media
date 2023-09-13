package com.tokopedia.tokochat.config.di.module

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object TokoChatConfigModule {

    @Provides
    @TokoChatQualifier
    fun provideUserSessionInterface(@TokoChatQualifier context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @TokoChatQualifier
    fun providesRemoteConfig(
        @TokoChatQualifier context: Context
    ): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    @TokoChatQualifier
    fun provideTokoChatRepository(
        @TokoChatQualifier retrofit: Retrofit,
        @TokoChatQualifier context: Context,
        @TokoChatQualifier babbleCourierClient: BabbleCourierClient,
        @TokoChatQualifier remoteConfig: RemoteConfig
    ): TokoChatRepository {
        return TokoChatRepository(retrofit, context, babbleCourierClient, remoteConfig)
    }
}
