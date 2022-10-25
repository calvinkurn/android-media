package com.tokochat.tokochat_config_common.di.component

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigContextModule
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigConversationModule
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigModule
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigNetworkModule
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.courier.TokoChatCourierClientProvider
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
    fun getCourierClientProvider(): TokoChatCourierClientProvider

    @TokoChatQualifier
    fun getRetrofit(): Retrofit

    @TokoChatQualifier
    fun getBabbleCourierClient(): BabbleCourierClient

    @TokoChatQualifier
    fun getContext(): Context
}
