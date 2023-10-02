package com.tokopedia.tokochat.config.di.component

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.courier.CourierConnection
import com.tokopedia.tokochat.config.di.module.TokoChatConfigContextModule
import com.tokopedia.tokochat.config.di.module.TokoChatConfigConversationModule
import com.tokopedia.tokochat.config.di.module.TokoChatConfigModule
import com.tokopedia.tokochat.config.di.module.TokoChatConfigNetworkModule
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.remoteconfig.RemoteConfig
import dagger.Component
import retrofit2.Retrofit

@Component(
    modules = [
        TokoChatConfigContextModule::class,
        TokoChatConfigConversationModule::class,
        TokoChatConfigModule::class,
        TokoChatConfigNetworkModule::class
    ]
)
interface TokoChatConfigComponent {

    @TokoChatQualifier
    fun getCourierConnection(): CourierConnection

    @TokoChatQualifier
    fun getRetrofit(): Retrofit

    @TokoChatQualifier
    fun getBabbleCourierClient(): BabbleCourierClient

    @TokoChatQualifier
    fun getContext(): Context

    @TokoChatQualifier
    fun getTokoChatRepository(): TokoChatRepository

    @TokoChatQualifier
    fun getRemoteConfig(): RemoteConfig
}
