package com.tokopedia.tokochat.config.di.module

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.courier.CourierConnection
import com.gojek.courier.config.CourierRemoteConfig
import com.google.gson.Gson
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.remoteconfig.TokoChatCourierRemoteConfigImpl
import com.tokopedia.tokochat.config.repository.courier.TokoChatBabbleCourierImpl
import com.tokopedia.tokochat.config.repository.courier.TokoChatCourierClientProvider
import com.tokopedia.tokochat.config.util.TokoChatCourierStateObservable
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object TokoChatConfigConversationModule {

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
    fun provideTokoChatBabbleCourier(
        @TokoChatQualifier courierConnection: CourierConnection,
        courierStateObservable: TokoChatCourierStateObservable,
        @TokoChatQualifier remoteConfig: RemoteConfig,
        @TokoChatQualifier userSession: UserSessionInterface
    ): BabbleCourierClient {
        return TokoChatBabbleCourierImpl(
            courierConnection,
            courierStateObservable,
            remoteConfig,
            userSession
        )
    }
}
