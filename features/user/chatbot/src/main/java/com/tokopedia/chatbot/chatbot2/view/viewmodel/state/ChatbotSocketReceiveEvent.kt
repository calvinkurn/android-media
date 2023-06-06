package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

import com.tokopedia.abstraction.base.view.adapter.Visitable

sealed class ChatbotSocketReceiveEvent {
    object StartTypingEvent : ChatbotSocketReceiveEvent()
    object EndTypingEvent : ChatbotSocketReceiveEvent()
    object ReadEvent : ChatbotSocketReceiveEvent()
    data class ReplyMessageEvent(val visitable: Visitable<*>) : ChatbotSocketReceiveEvent()
}
