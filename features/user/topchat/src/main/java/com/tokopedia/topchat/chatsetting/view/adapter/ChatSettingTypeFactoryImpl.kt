package com.tokopedia.topchat.chatsetting.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatsetting.data.ChatSetting
import com.tokopedia.topchat.chatsetting.view.adapter.viewholder.ChatSettingViewHolder

class ChatSettingTypeFactoryImpl: BaseAdapterTypeFactory(), ChatSettingTypeFactory {

    override fun type(chatSetting: ChatSetting): Int {
        return ChatSettingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ChatSettingViewHolder.LAYOUT -> ChatSettingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}