package com.tokochat.tokochat_config_common.di

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.courier.CourierConnection
import com.gojek.courier.config.CourierRemoteConfig
import com.google.gson.Gson
import com.tokochat.tokochat_config_common.di.TokoChatNetworkModule.RETROFIT_NAME
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import com.tokochat.tokochat_config_common.repository.courier.TokoChatBabbleCourierImpl
import com.tokochat.tokochat_config_common.repository.courier.TokoChatCourierClientProvider
import com.tokochat.tokochat_config_common.util.TokoChatCourierRemoteConfigImpl
import com.tokochat.tokochat_config_common.util.TokoChatCourierStateObservable
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
object TokoChatConfigModule {

    @TokoChatConfigScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @TokoChatConfigScope
    @Provides
    fun providesRemoteConfig(
        @ApplicationContext context: Context
    ): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @TokoChatConfigScope
    @Provides
    fun provideTokoChatRepository(
        @Named(RETROFIT_NAME) retrofit: Retrofit,
        @ApplicationContext context: Context,
        babbleCourierClient: BabbleCourierClient
    ): TokoChatRepository {
        return TokoChatRepository(retrofit, context, babbleCourierClient)
    }

    @TokoChatConfigScope
    @Provides
    fun provideTokoChatBabbleCourier(
        courierConnection: CourierConnection,
        courierStateObservable: TokoChatCourierStateObservable,
        remoteConfig: RemoteConfig
    ): BabbleCourierClient {
        return TokoChatBabbleCourierImpl(
            courierConnection, courierStateObservable, remoteConfig)
    }

    @TokoChatConfigScope
    @Provides
    fun provideTokoChatCourierConnection(
        @ApplicationContext context: Context,
        gson: Gson,
        @Named(RETROFIT_NAME) retrofit: Retrofit,
        userSession: UserSessionInterface,
        courierRemoteConfig: CourierRemoteConfig
    ): CourierConnection {
        val provider = TokoChatCourierClientProvider(
            context, gson, retrofit, userSession, courierRemoteConfig
        )
        return provider.initializeCourierConnection()
    }


    @TokoChatConfigScope
    @Provides
    fun provideTokoChatCourierRemoteConfig(
        remoteConfig: RemoteConfig
    ): CourierRemoteConfig {
        return TokoChatCourierRemoteConfigImpl(remoteConfig)
    }
}
