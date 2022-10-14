package com.tokopedia.tokochat.di

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.courier.CourierConnection
import com.gojek.courier.config.CourierRemoteConfig
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokochat.util.TokoChatCourierStateObservable
import com.tokopedia.tokochat.data.repository.courier.TokoChatBabbleCourierImpl
import com.tokopedia.tokochat.data.repository.courier.TokoChatCourierClientProvider
import com.tokopedia.tokochat.data.repository.TokoChatRepository
import com.tokopedia.tokochat.di.TokoChatNetworkModule.RETROFIT_NAME
import com.tokopedia.tokochat.util.TokoChatCourierRemoteConfigImpl
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
    fun provideTokoChatRepository(
        @Named(RETROFIT_NAME) retrofit: Retrofit,
        @ApplicationContext context: Context,
        babbleCourierClient: BabbleCourierClient
    ): TokoChatRepository {
        return TokoChatRepository(retrofit, context, babbleCourierClient)
    }

    @TokoChatScope
    @Provides
    fun provideTokoChatBabbleCourier(
        courierConnection: CourierConnection,
        courierStateObservable: TokoChatCourierStateObservable,
        remoteConfig: RemoteConfig
    ): BabbleCourierClient {
        return TokoChatBabbleCourierImpl(
            courierConnection, courierStateObservable, remoteConfig)
    }

    @TokoChatScope
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


    @TokoChatScope
    @Provides
    fun provideTokoChatCourierRemoteConfig(
        remoteConfig: RemoteConfig
    ): CourierRemoteConfig {
        return TokoChatCourierRemoteConfigImpl(remoteConfig)
    }

    @TokoChatScope
    @Provides
    fun providesRemoteConfig(
        @ApplicationContext context: Context
    ): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}
