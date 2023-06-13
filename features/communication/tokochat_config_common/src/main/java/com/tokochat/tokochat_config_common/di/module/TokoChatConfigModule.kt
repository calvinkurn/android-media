package com.tokochat.tokochat_config_common.di.module

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
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
        @TokoChatQualifier babbleCourierClient: BabbleCourierClient
    ): TokoChatRepository {
        return TokoChatRepository(retrofit, context, babbleCourierClient)
    }
}
