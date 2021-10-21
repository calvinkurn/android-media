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

    fun type(imageAnnouncementUiModel: ImageAnnouncementUiModel): Int

    fun type(imageUploadUiModel: ImageUploadUiModel): Int

    fun type(fallbackAttachmentUiModel: FallbackAttachmentUiModel): Int

    fun type(productAttachmentUiModel: ProductAttachmentUiModel): Int

    fun type(attachInvoiceSentUiModel: AttachInvoiceSentUiModel): Int

    fun type(bannedAttachmentUiModel: BannedProductAttachmentUiModel): Int

    fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*>
}
