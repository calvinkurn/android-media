package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.chat_common.data.AttachInvoiceSentViewModel
import com.tokopedia.chat_common.data.FallbackAttachmentViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactoryImpl
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingBanner
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingFraudAlert
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.AttachedInvoiceViewHolder.InvoiceThumbnailListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.fallback.FallbackMessageViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.fallback.LeftFallbackMessageViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.fallback.RightFallbackMessageViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble.ChatMessageViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble.LeftChatMessageViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble.RightChatMessageViewHolder
import com.tokopedia.topchat.chatroom.view.listener.DualAnnouncementListener
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.QuotationUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel

open class TopChatTypeFactoryImpl(
        imageAnnouncementListener: ImageAnnouncementListener,
        private val chatLinkHandlerListener: ChatLinkHandlerListener,
        private val imageUploadListener: ImageUploadListener,
        productAttachmentListener: ProductAttachmentListener,
        private val imageDualAnnouncementListener: DualAnnouncementListener,
        private val voucherListener: TopChatVoucherListener,
        private val invoiceThumbnailListener: InvoiceThumbnailListener,
        private val quotationListener: QuotationViewHolder.QuotationListener
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
        } else if (chat is FallbackAttachmentViewModel) {
            if (chat.isOpposite) FallbackMessageViewHolder.TYPE_LEFT else FallbackMessageViewHolder.TYPE_RIGHT
        } else {
            default
        }
    }

    override fun type(imageDualAnnouncementViewModel: ImageDualAnnouncementUiModel): Int {
        return ImageDualAnnouncementViewHolder.LAYOUT
    }

    override fun type(voucherViewModel: TopChatVoucherUiModel): Int {
        return TopChatVoucherViewHolder.LAYOUT
    }

    override fun type(attachInvoiceSentViewModel: AttachInvoiceSentViewModel): Int {
        return AttachedInvoiceViewHolder.LAYOUT
    }

    override fun type(imageUploadViewModel: ImageUploadViewModel): Int {
        return TopchatImageUploadViewHolder.LAYOUT
    }

    override fun type(roomSettingBanner: RoomSettingBanner): Int {
        return RoomSettingBannerViewHolder.LAYOUT
    }

    override fun type(roomSettingFraudAlert: RoomSettingFraudAlert): Int {
        return RoomSettingFraudAlertViewHolder.LAYOUT
    }

    override fun type(quotationViewModel: QuotationUiModel): Int {
        return QuotationViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel): Int {
        return TopchatEmptyViewHolder.LAYOUT
    }

    // Check if chat bubble first, if not return default ViewHolder
    override fun createViewHolder(parent: ViewGroup, type: Int): AbstractViewHolder<*> {
        val layoutRes = when (type) {
            ChatMessageViewHolder.TYPE_LEFT -> LeftChatMessageViewHolder.LAYOUT
            ChatMessageViewHolder.TYPE_RIGHT -> RightChatMessageViewHolder.LAYOUT
            FallbackMessageViewHolder.TYPE_LEFT -> LeftFallbackMessageViewHolder.LAYOUT
            FallbackMessageViewHolder.TYPE_RIGHT -> RightFallbackMessageViewHolder.LAYOUT
            else -> type
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return createViewHolder(view, layoutRes)
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TopchatEmptyViewHolder.LAYOUT -> TopchatEmptyViewHolder(parent)
            QuotationViewHolder.LAYOUT -> QuotationViewHolder(parent, chatLinkHandlerListener, quotationListener)
            RoomSettingBannerViewHolder.LAYOUT -> RoomSettingBannerViewHolder(parent)
            RoomSettingFraudAlertViewHolder.LAYOUT -> RoomSettingFraudAlertViewHolder(parent)
            TopchatImageUploadViewHolder.LAYOUT -> TopchatImageUploadViewHolder(parent, imageUploadListener)
            LeftFallbackMessageViewHolder.LAYOUT -> LeftFallbackMessageViewHolder(parent, chatLinkHandlerListener)
            RightFallbackMessageViewHolder.LAYOUT -> RightFallbackMessageViewHolder(parent, chatLinkHandlerListener)
            LeftChatMessageViewHolder.LAYOUT -> LeftChatMessageViewHolder(parent, chatLinkHandlerListener)
            RightChatMessageViewHolder.LAYOUT -> RightChatMessageViewHolder(parent, chatLinkHandlerListener)
            ImageDualAnnouncementViewHolder.LAYOUT -> ImageDualAnnouncementViewHolder(parent, imageDualAnnouncementListener)
            TopChatVoucherViewHolder.LAYOUT -> TopChatVoucherViewHolder(parent, voucherListener)
            AttachedInvoiceViewHolder.LAYOUT -> AttachedInvoiceViewHolder(parent, invoiceThumbnailListener)
            else -> super.createViewHolder(parent, type)
        }
    }

}
