package com.tokopedia.chat_common.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.*

/**
 * @author by nisie on 27/11/18.
 */

interface BaseChatTypeFactory {

    fun type(messageViewModel: MessageViewModel): Int

    fun type(typingViewModel: TypingChatModel): Int

    fun type(imageAnnouncementViewModel: ImageAnnouncementViewModel): Int

    fun type(imageUploadViewModel: ImageUploadViewModel): Int

    fun type(fallbackAttachmentViewModel: FallbackAttachmentViewModel): Int

    fun type(productAttachmentViewModel: ProductAttachmentViewModel): Int

    fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*>
}
