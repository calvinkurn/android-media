package com.tokopedia.chat_common.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.data.TypingChatModel

/**
 * @author by nisie on 27/11/18.
 */

interface BaseChatTypeFactory {

    fun type(messageViewModel: MessageViewModel): Int

    fun type(typingViewModel: TypingChatModel): Int

    fun type(imageAnnouncementViewModel: ImageAnnouncementViewModel):

    fun type(imageUploadViewModel: ImageUploadViewModel): Int

    fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*>
}
