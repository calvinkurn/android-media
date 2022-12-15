package com.tokochat.tokochat_config_common.di.component

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.courier.CourierConnection
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigContextModule
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigConversationModule
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigModule
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigNetworkModule
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
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
}
