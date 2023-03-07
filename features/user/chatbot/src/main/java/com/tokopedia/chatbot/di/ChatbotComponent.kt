package com.tokopedia.chatbot.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.chatbot.view.activity.ChatbotOnboardingActivity
import com.tokopedia.chatbot.view.activity.ContactUsMigrationActivity
import com.tokopedia.chatbot.view.fragment.ChatBotProvideRatingFragment
import com.tokopedia.chatbot.view.fragment.ChatbotFragment
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

    fun inject(chatbotFragment: ChatbotFragment)

    fun inject(contactUsMigrationActivity: ContactUsMigrationActivity)

    fun inject(chatbotProvideRatingFragment : ChatBotProvideRatingFragment)

    fun inject(chatbotOnboardingActivity: ChatbotOnboardingActivity)

}
