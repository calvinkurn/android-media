package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.chat_common.data.AttachInvoiceSentViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactoryImpl
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.AttachedInvoiceViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.AttachedInvoiceViewHolder.InvoiceThumbnailListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ImageDualAnnouncementViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.SecurityInfoChatViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopChatVoucherViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble.ChatMessageViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble.LeftChatMessageViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble.RightChatMessageViewHolder
import com.tokopedia.topchat.chatroom.view.listener.DualAnnouncementListener
import com.tokopedia.topchat.chatroom.view.listener.SecurityInfoListener
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SecurityInfoViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherViewModel

open class TopChatTypeFactoryImpl(
        imageAnnouncementListener: ImageAnnouncementListener,
        chatLinkHandlerListener: ChatLinkHandlerListener,
        imageUploadListener: ImageUploadListener,
        productAttachmentListener: ProductAttachmentListener,
        private val imageDualAnnouncementListener: DualAnnouncementListener,
        private val securityInfoListener: SecurityInfoListener,
        private val voucherListener: TopChatVoucherListener,
        private val invoiceThumbnailListener: InvoiceThumbnailListener
) : BaseChatTypeFactoryImpl(
        imageAnnouncementListener,
        chatLinkHandlerListener,
        imageUploadListener,
        productAttachmentListener
), TopChatTypeFactory {

    // Check if chat bubble first, if not return default impl
    override fun getItemViewType(visitables: List<Visitable<*>>, position: Int, default: Int): Int {
        if (position < 0 || position >= visitables.size) {
            return HideViewHolder.LAYOUT
        }
        val chat = visitables[position]
        return if (chat is MessageViewModel) {
            if (chat.isSender) ChatMessageViewHolder.TYPE_RIGHT else ChatMessageViewHolder.TYPE_LEFT
        } else {
            default
        }
    }

    override fun type(securityInfoViewModel: SecurityInfoViewModel): Int {
        return SecurityInfoChatViewHolder.LAYOUT
    }

    override fun type(imageDualAnnouncementViewModel: ImageDualAnnouncementViewModel): Int {
        return ImageDualAnnouncementViewHolder.LAYOUT
    }

    override fun type(voucherViewModel: TopChatVoucherViewModel): Int {
        return TopChatVoucherViewHolder.LAYOUT
    }

    override fun type(attachInvoiceSentViewModel: AttachInvoiceSentViewModel): Int {
        return AttachedInvoiceViewHolder.LAYOUT
    }

    // Check if chat bubble first, if not return default ViewHolder
    override fun createViewHolder(parent: ViewGroup, type: Int): AbstractViewHolder<*> {
        val layoutRes = when (type) {
            ChatMessageViewHolder.TYPE_LEFT -> LeftChatMessageViewHolder.LAYOUT
            ChatMessageViewHolder.TYPE_RIGHT -> RightChatMessageViewHolder.LAYOUT
            else -> type
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return createViewHolder(view, layoutRes)
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            LeftChatMessageViewHolder.LAYOUT -> LeftChatMessageViewHolder(parent)
            RightChatMessageViewHolder.LAYOUT -> RightChatMessageViewHolder(parent)
            SecurityInfoChatViewHolder.LAYOUT -> SecurityInfoChatViewHolder(parent, securityInfoListener)
            ImageDualAnnouncementViewHolder.LAYOUT -> ImageDualAnnouncementViewHolder(parent, imageDualAnnouncementListener)
            TopChatVoucherViewHolder.LAYOUT -> TopChatVoucherViewHolder(parent, voucherListener)
            AttachedInvoiceViewHolder.LAYOUT -> AttachedInvoiceViewHolder(parent, invoiceThumbnailListener)
            else -> super.createViewHolder(parent, type)
        }
    }

}
