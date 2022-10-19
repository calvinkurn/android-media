package com.tokochat.tokochat_config_common.di

import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.courier.CourierConnection
import com.tokochat.tokochat_config_common.di.TokoChatNetworkModule.RETROFIT_NAME
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Named

@TokoChatConfigScope
@Component(
    modules = [
        TokoChatConfigModule::class,
        TokoChatNetworkModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface TokoChatConfigComponent{

    @Named(RETROFIT_NAME)
    fun retrofit(): Retrofit

    fun babbleCourierClient(): BabbleCourierClient

    fun courierConnection(): CourierConnection
}
