package com.tokopedia.tokochat.stub.di

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.courier.CourierConnection
import com.gojek.courier.config.CourierRemoteConfig
import com.google.gson.Gson
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.remote_config.TokoChatCourierRemoteConfigImpl
import com.tokochat.tokochat_config_common.repository.courier.TokoChatCourierClientProvider
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokochat.stub.common.BabbleCourierClientStub
import com.tokopedia.tokochat.stub.common.ConversationsPreferencesStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class TokoChatCourierConversationModule {

    @Provides
    @TokoChatQualifier
    fun provideTokoChatCourierRemoteConfig(
        @TokoChatQualifier remoteConfig: RemoteConfig
    ): CourierRemoteConfig {
        return TokoChatCourierRemoteConfigImpl(remoteConfig)
    }

    @Provides
    @TokoChatQualifier
    fun provideTokoChatCourierConnection(
        @TokoChatQualifier context: Context,
        @TokoChatQualifier gson: Gson,
        @TokoChatQualifier retrofit: Retrofit,
        @TokoChatQualifier userSession: UserSessionInterface,
        @TokoChatQualifier courierRemoteConfig: CourierRemoteConfig
    ): CourierConnection {
        val provider = TokoChatCourierClientProvider(
            context,
            gson,
            retrofit,
            userSession,
            courierRemoteConfig
        )
        return provider.getCourierConnection()
    }

    @Provides
    @TokoChatQualifier
    fun provideTokoChatBabbleCourier(): BabbleCourierClient {
        return BabbleCourierClientStub()
    }

    @Provides
    @TokoChatQualifier
    fun provideConversationsPreferences(
        @TokoChatQualifier context: Context
    ): ConversationsPreferencesStub {
        return ConversationsPreferencesStub(context)
    }
}
