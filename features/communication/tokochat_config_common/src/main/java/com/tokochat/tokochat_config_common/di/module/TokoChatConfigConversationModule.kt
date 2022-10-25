package com.tokochat.tokochat_config_common.di.module

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.courier.config.CourierRemoteConfig
import com.google.gson.Gson
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.courier.TokoChatBabbleCourierImpl
import com.tokochat.tokochat_config_common.repository.courier.TokoChatCourierClientProvider
import com.tokochat.tokochat_config_common.util.TokoChatCourierRemoteConfigImpl
import com.tokochat.tokochat_config_common.util.TokoChatCourierStateObservable
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
    fun provideTokoChatCourierClientProvider(
        @TokoChatQualifier context: Context,
        @TokoChatQualifier gson: Gson,
        @TokoChatQualifier retrofit: Retrofit,
        @TokoChatQualifier userSession: UserSessionInterface,
        @TokoChatQualifier courierRemoteConfig: CourierRemoteConfig
    ): TokoChatCourierClientProvider {
        return TokoChatCourierClientProvider(
            context, gson, retrofit, userSession, courierRemoteConfig
        )
    }

    @Provides
    @TokoChatQualifier
    fun provideTokoChatBabbleCourier(
        courierStateObservable: TokoChatCourierStateObservable,
        @TokoChatQualifier remoteConfig: RemoteConfig
    ): BabbleCourierClient {
        return TokoChatBabbleCourierImpl(
            courierStateObservable, remoteConfig
        )
    }
}
