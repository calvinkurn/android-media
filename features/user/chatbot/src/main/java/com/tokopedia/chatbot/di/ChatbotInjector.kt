package com.tokopedia.chatbot.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

object ChatbotInjector {
    
    private var chatbotComponent : ChatbotComponent? = null 
    
    fun get(context: Context) : ChatbotComponent = synchronized(this) {
        return chatbotComponent ?: DaggerChatbotComponent.builder()
            .baseAppComponent(
                (context.applicationContext as BaseMainApplication).baseAppComponent
            )
            .chatbotModule(ChatbotModule(context))
            .build()
    }

    fun set(component : ChatbotComponent) = synchronized(this) {
        chatbotComponent = component
    }
    
}

