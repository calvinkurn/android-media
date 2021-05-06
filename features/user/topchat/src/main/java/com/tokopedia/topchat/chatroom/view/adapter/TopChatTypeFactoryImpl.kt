package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactoryImpl
import com.tokopedia.chat_common.view.adapter.viewholder.ProductAttachmentViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingBanner
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingFraudAlert
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.AttachedInvoiceViewHolder.InvoiceThumbnailListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopchatProductAttachmentListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble.BannedRightChatMessageViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble.ChatMessageViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble.LeftChatMessageViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble.RightChatMessageViewHolder
import com.tokopedia.topchat.chatroom.view.listener.DualAnnouncementListener
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.*
import com.tokopedia.topchat.chatroom.view.viewmodel.BroadcastSpamHandlerUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.QuotationUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel

open class TopChatTypeFactoryImpl constructor(
        private val imageAnnouncementListener: ImageAnnouncementListener,
        private val chatLinkHandlerListener: ChatLinkHandlerListener,
        private val imageUploadListener: ImageUploadListener,
        private val productAttachmentListener: TopchatProductAttachmentListener,
        private val imageDualAnnouncementListener: DualAnnouncementListener,
        private val voucherListener: TopChatVoucherListener,
        private val invoiceThumbnailListener: InvoiceThumbnailListener,
        private val quotationListener: QuotationViewHolder.QuotationListener,
        private val deferredAttachment: DeferredViewHolderAttachment,
        private val commonListener: CommonViewHolderListener,
        private val searchListener: SearchListener,
        private val broadcastHandlingListener: BroadcastSpamHandlerViewHolder.Listener,
        private val fraudAlertListener: RoomSettingFraudAlertViewHolder.Listener,
        private val reviewListener: ReviewViewHolder.Listener
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
            if (chat.isSender) {
                if (chat.isBanned()) {
                    ChatMessageViewHolder.TYPE_RIGHT_BANNED
                } else {
                    ChatMessageViewHolder.TYPE_RIGHT
                }
            } else {
                ChatMessageViewHolder.TYPE_LEFT
            }
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

    override fun type(productCarouselUiModel: ProductCarouselUiModel): Int {
        return ProductCarouselListAttachmentViewHolder.LAYOUT
    }

    override fun type(headerDateUiModel: HeaderDateUiModel): Int {
        return HeaderDateViewHolder.LAYOUT
    }

    override fun type(stickerUiModel: StickerUiModel): Int {
        return StickerMessageViewHolder.LAYOUT
    }

    override fun type(broadcastSpamHandlerUiModel: BroadcastSpamHandlerUiModel): Int {
        return BroadcastSpamHandlerViewHolder.LAYOUT
    }

    override fun type(broadCastUiModel: BroadCastUiModel): Int {
        return BroadcastViewHolder.LAYOUT
    }

    override fun type(reviewUiModel: ReviewUiModel): Int {
        return ReviewViewHolder.LAYOUT
    }

    override fun type(productAttachmentViewModel: ProductAttachmentViewModel): Int {
        return TopchatProductAttachmentViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingMoreModel): Int {
        return TopchatLoadingMoreViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return TopchatLoadingModelViewHolder.LAYOUT
    }

    override fun type(imageAnnouncementViewModel: ImageAnnouncementViewModel): Int {
        return TopchatImageAnnouncementViewHolder.LAYOUT
    }

    override fun type(bannedAttachmentViewModel: BannedProductAttachmentViewModel): Int {
        return TopchatBannedProductAttachmentViewHolder.LAYOUT
    }

    // Check if chat bubble first, if not return default ViewHolder
    override fun createViewHolder(
            parent: ViewGroup,
            type: Int,
            productCarouselListListener: ProductCarouselListAttachmentViewHolder.Listener,
            adapterListener: AdapterListener
    ): AbstractViewHolder<*> {
        val layoutRes = when (type) {
            ChatMessageViewHolder.TYPE_LEFT -> LeftChatMessageViewHolder.LAYOUT
            ChatMessageViewHolder.TYPE_RIGHT -> RightChatMessageViewHolder.LAYOUT
            ChatMessageViewHolder.TYPE_RIGHT_BANNED -> BannedRightChatMessageViewHolder.LAYOUT
            else -> type
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return createViewHolder(view, layoutRes, productCarouselListListener, adapterListener)
    }

    private fun createViewHolder(
            parent: View,
            type: Int,
            productCarouselListListener: ProductCarouselListAttachmentViewHolder.Listener,
            adapterListener: AdapterListener
    ): AbstractViewHolder<*> {
        return when (type) {
            ProductCarouselListAttachmentViewHolder.LAYOUT -> ProductCarouselListAttachmentViewHolder(parent, productAttachmentListener, productCarouselListListener, deferredAttachment, searchListener, commonListener, adapterListener)
            BroadcastViewHolder.LAYOUT -> BroadcastViewHolder(
                    parent, imageAnnouncementListener, voucherListener, productAttachmentListener,
                    productCarouselListListener, deferredAttachment, searchListener,
                    commonListener, adapterListener, chatLinkHandlerListener
            )
            LeftChatMessageViewHolder.LAYOUT -> LeftChatMessageViewHolder(
                    parent, chatLinkHandlerListener, commonListener, adapterListener
            )
            RightChatMessageViewHolder.LAYOUT -> RightChatMessageViewHolder(
                    parent, chatLinkHandlerListener, commonListener, adapterListener
            )
            BannedRightChatMessageViewHolder.LAYOUT -> BannedRightChatMessageViewHolder(
                    parent, chatLinkHandlerListener, commonListener, adapterListener
            )
            TopchatProductAttachmentViewHolder.LAYOUT -> TopchatProductAttachmentViewHolder(
                    parent, productAttachmentListener, deferredAttachment,
                    searchListener, commonListener, adapterListener
            )
            ReviewViewHolder.LAYOUT -> ReviewViewHolder(
                    parent, reviewListener, deferredAttachment, adapterListener
            )
            else -> createViewHolder(parent, type)
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TopchatBannedProductAttachmentViewHolder.LAYOUT -> TopchatBannedProductAttachmentViewHolder(parent, productAttachmentListener)
            TopchatImageAnnouncementViewHolder.LAYOUT -> TopchatImageAnnouncementViewHolder(parent, imageAnnouncementListener)
            BroadcastSpamHandlerViewHolder.LAYOUT -> BroadcastSpamHandlerViewHolder(parent, broadcastHandlingListener)
            TopchatLoadingModelViewHolder.LAYOUT -> TopchatLoadingModelViewHolder(parent)
            TopchatLoadingMoreViewHolder.LAYOUT -> TopchatLoadingMoreViewHolder(parent)
            StickerMessageViewHolder.LAYOUT -> StickerMessageViewHolder(parent)
            HeaderDateViewHolder.LAYOUT -> HeaderDateViewHolder(parent)
            ProductAttachmentViewHolder.LAYOUT -> TopchatOldProductAttachmentViewHolder(parent, productAttachmentListener)
            TopchatEmptyViewHolder.LAYOUT -> TopchatEmptyViewHolder(parent)
            QuotationViewHolder.LAYOUT -> QuotationViewHolder(parent, chatLinkHandlerListener, quotationListener)
            RoomSettingBannerViewHolder.LAYOUT -> RoomSettingBannerViewHolder(parent)
            RoomSettingFraudAlertViewHolder.LAYOUT -> RoomSettingFraudAlertViewHolder(parent, fraudAlertListener)
            TopchatImageUploadViewHolder.LAYOUT -> TopchatImageUploadViewHolder(parent, imageUploadListener)
            ImageDualAnnouncementViewHolder.LAYOUT -> ImageDualAnnouncementViewHolder(parent, imageDualAnnouncementListener)
            TopChatVoucherViewHolder.LAYOUT -> TopChatVoucherViewHolder(parent, voucherListener)
            AttachedInvoiceViewHolder.LAYOUT -> AttachedInvoiceViewHolder(parent, invoiceThumbnailListener, deferredAttachment)
            else -> super.createViewHolder(parent, type)
        }
    }

}
