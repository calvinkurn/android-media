package com.tokopedia.topchat.chatsetting.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingDividerUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingTitleUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ItemChatSettingUiModel
import com.tokopedia.topchat.chatsetting.view.adapter.viewholder.ChatSettingDividerViewHolder
import com.tokopedia.topchat.chatsetting.view.adapter.viewholder.ChatSettingTitleViewHolder
import com.tokopedia.topchat.chatsetting.view.adapter.viewholder.ChatSettingViewHolder

class ChatSettingTypeFactoryImpl(
        val listener: ChatSettingViewHolder.ChatSettingListener
) : BaseAdapterTypeFactory(), ChatSettingTypeFactory {

    override fun type(chatSettingDividerUiModel: ChatSettingDividerUiModel): Int {
        return ChatSettingDividerViewHolder.LAYOUT
    }

    override fun type(chatSettingTitleUiModel: ChatSettingTitleUiModel): Int {
        return ChatSettingTitleViewHolder.LAYOUT
    }

    override fun type(itemChatSettingUiModel: ItemChatSettingUiModel): Int {
        return ChatSettingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ChatSettingTitleViewHolder.LAYOUT -> ChatSettingTitleViewHolder(parent)
            ChatSettingDividerViewHolder.LAYOUT -> ChatSettingDividerViewHolder(parent)
            ChatSettingViewHolder.LAYOUT -> ChatSettingViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}