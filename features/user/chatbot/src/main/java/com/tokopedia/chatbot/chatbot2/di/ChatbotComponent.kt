package com.tokopedia.chatbot.chatbot2.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.chatbot.chatbot2.view.activity.ChatbotOnboardingActivity
import com.tokopedia.chatbot.chatbot2.view.fragment.ChatBotProvideRatingFragment
import com.tokopedia.chatbot.chatbot2.view.fragment.ChatbotFragment2
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import dagger.Component

/**
 * @author by nisie on 12/12/18.
 */
@ChatbotScope
@Component(
    modules = arrayOf(
        ChatbotModule::class,
        ChatViewModelModule::class,
        MediaUploaderModule::class
    ),
    dependencies = arrayOf(BaseAppComponent::class)
)
interface ChatbotComponent {

    fun inject(chatbotFragment2: ChatbotFragment2)

    fun inject(chatbotProvideRatingFragment: ChatBotProvideRatingFragment)

    fun inject(chatbotOnboardingActivity: ChatbotOnboardingActivity)
}
