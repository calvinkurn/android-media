package com.tokopedia.chatbot.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.chatbot.view.fragment.ChatbotFragment
import dagger.Component

/**
 * @author by nisie on 12/12/18.
 */
@ChatbotScope
@Component(modules = arrayOf(ChatbotModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface ChatbotComponent {

    fun inject(chatbotFragment: ChatbotFragment)

}