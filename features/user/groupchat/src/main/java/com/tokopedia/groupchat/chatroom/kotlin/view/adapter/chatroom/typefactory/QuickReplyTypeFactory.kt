package com.tokopedia.groupchat.chatroom.kotlin.view.adapter.chatroom.typefactory

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.chatroom.GroupChatQuickReplyItemViewModel

/**
 * @author by StevenFredian on 07/06/18.
 */

interface QuickReplyTypeFactory {

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

    fun type(groupChatQuickReplyViewModel: GroupChatQuickReplyItemViewModel): Int
}
