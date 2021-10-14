package com.tokopedia.chat_common.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.*

/**
 * @author by nisie on 27/11/18.
 */

interface BaseChatTypeFactory {

    fun type(messageUiModel: MessageUiModel): Int

    fun type(typingViewModel: TypingChatModel): Int

    fun type(imageAnnouncementViewModel: ImageAnnouncementViewModel): Int

    fun type(imageUploadViewModel: ImageUploadViewModel): Int

    fun type(fallbackAttachmentUiModel: FallbackAttachmentUiModel): Int

    fun type(productAttachmentViewModel: ProductAttachmentViewModel): Int

    fun type(attachInvoiceSentViewModel: AttachInvoiceSentViewModel): Int

    fun type(bannedAttachmentUiModel: BannedProductAttachmentUiModel): Int

    fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*>
}
