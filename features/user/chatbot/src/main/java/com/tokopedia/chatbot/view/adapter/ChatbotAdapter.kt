package com.tokopedia.chatbot.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.BaseChatAdapter

/**
 * @author by nisie on 27/11/18.
 */

class ChatbotAdapter(adapterTypeFactory: ChatbotTypeFactoryImpl,
                     listChat: ArrayList<Visitable<*>>)
    : BaseChatAdapter(adapterTypeFactory, listChat) {

}