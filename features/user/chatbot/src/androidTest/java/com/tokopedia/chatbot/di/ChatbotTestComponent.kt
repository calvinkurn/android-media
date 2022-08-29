package com.tokopedia.chatbot.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.chatbot.ChatbotViewIdGenerator
import dagger.Component

/**
 * @author by nisie on 12/12/18.
 */
@ChatbotScope
@Component(
    modules = arrayOf(
        ChatbotTestModule::class,
        ChatViewModelModule::class
    ),
    dependencies = arrayOf(BaseAppComponent::class)
)
interface ChatbotTestComponent : ChatbotComponent {
    fun inject(id : ChatbotViewIdGenerator)
}