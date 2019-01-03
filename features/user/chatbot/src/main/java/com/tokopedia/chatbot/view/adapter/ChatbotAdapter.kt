package com.tokopedia.chatbot.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.chat_common.BaseChatAdapter

/**
 * @author by nisie on 27/11/18.
 */

class ChatbotAdapter(adapterTypeFactory: ChatbotTypeFactoryImpl)
    : BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(adapterTypeFactory) {

}