package com.tokopedia.topchat.chatroom.view.adapter.typefactory

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
import com.tokopedia.topchat.chatroom.domain.TopChatRoomMessageTypeEnum
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingBannerUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingFraudAlertUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.srw.SrwBubbleUiModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.AttachedInvoiceViewHolder.InvoiceThumbnailListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.broadcast.BroadcastSpamHandlerViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.broadcast.BroadcastViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.broadcast.TopChatRoomBroadcastFlashSaleViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.broadcast.TopChatRoomBroadcastPromoViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopChatRoomOrderCancellationListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopchatProductAttachmentListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.autoreply.TopChatRoomAutoReplyViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.banned.BannedChatMessageViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.regular.TopChatRoomBubbleMessageViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ordercancellation.TopChatRoomOrderCancellationViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.ProductBundlingCardViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.ProductBundlingCarouselViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw.SrwBubbleViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.voucher.TopChatOldVoucherViewHolder
import com.tokopedia.topchat.chatroom.view.custom.message.ReplyBubbleAreaMessage
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.TopChatRoomFlexBoxListener
import com.tokopedia.topchat.chatroom.view.listener.DualAnnouncementListener
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomBroadcastBannerListener
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomBroadcastProductListener
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.*
import com.tokopedia.topchat.chatroom.view.uimodel.BroadcastSpamHandlerUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.ImageDualAnnouncementUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.ReminderTickerUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherCarouselUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherUiModel
import com.tokopedia.user.session.UserSessionInterface

open class TopChatRoomTypeFactoryImpl constructor(
    private val imageAnnouncementListener: ImageAnnouncementListener,
    private val bannerBroadcastListener: TopChatRoomBroadcastBannerListener,
    private val chatLinkHandlerListener: ChatLinkHandlerListener,
    private val imageUploadListener: ImageUploadListener,
    private val productAttachmentListener: TopchatProductAttachmentListener,
    private val productBroadcastListener: TopChatRoomBroadcastProductListener,
    private val imageDualAnnouncementListener: DualAnnouncementListener,
    private val voucherListener: TopChatRoomVoucherListener,
    private val invoiceThumbnailListener: InvoiceThumbnailListener,
    private val deferredAttachment: DeferredViewHolderAttachment,
    private val commonListener: CommonViewHolderListener,
    private val searchListener: SearchListener,
    private val broadcastHandlingListener: BroadcastSpamHandlerViewHolder.Listener,
    private val fraudAlertListener: RoomSettingFraudAlertViewHolder.Listener,
    private val reviewListener: ReviewViewHolder.Listener,
    private val srwBubbleListener: SrwBubbleViewHolder.Listener,
    private val chatMsgListener: TopChatRoomFlexBoxListener,
    private val replyBubbleListener: ReplyBubbleAreaMessage.Listener,
    private val listener: ReminderTickerViewHolder.Listener,
    private val productBundlingListener: ProductBundlingListener,
    private val censorListener: BannedChatMessageViewHolder.TopChatMessageCensorListener,
    private val orderCancellationListener: TopChatRoomOrderCancellationListener,
    private val userSession: UserSessionInterface
) : BaseChatTypeFactoryImpl(
    imageAnnouncementListener,
    chatLinkHandlerListener,
    imageUploadListener,
    productAttachmentListener
),
    TopChatRoomTypeFactory {

    // Check if chat bubble first, if not return default impl
    override fun getItemViewType(visitables: List<Visitable<*>>, position: Int, default: Int): Int {
        if (position < 0 || position >= visitables.size) {
            return HideViewHolder.LAYOUT
        }
        val chat = visitables[position]
        return if (chat is MessageUiModel && chat.isBanned()) {
            TopChatRoomBubbleMessageViewHolder.TYPE_BANNED
        } else {
            default
        }
    }

    override fun type(messageUiModel: MessageUiModel): Int {
        return TopChatRoomBubbleMessageViewHolder.LAYOUT
    }

    override fun type(fallbackAttachmentUiModel: FallbackAttachmentUiModel): Int {
        return type(fallbackAttachmentUiModel as MessageUiModel)
    }

    override fun type(imageDualAnnouncementViewModel: ImageDualAnnouncementUiModel): Int {
        return ImageDualAnnouncementViewHolder.LAYOUT
    }

    override fun type(voucherViewModel: TopChatRoomVoucherUiModel): Int {
        return TopChatOldVoucherViewHolder.LAYOUT
    }

    override fun type(voucherCarouselUiModel: TopChatRoomVoucherCarouselUiModel): Int {
        return 0 // No implementation yet, but used as new broadcast component
    }

    override fun type(attachInvoiceSentUiModel: AttachInvoiceSentUiModel): Int {
        return AttachedInvoiceViewHolder.LAYOUT
    }

    override fun type(imageUploadUiModel: ImageUploadUiModel): Int {
        return TopchatImageUploadViewHolder.LAYOUT
    }

    override fun type(roomSettingBannerUiModel: RoomSettingBannerUiModel): Int {
        return RoomSettingBannerViewHolder.LAYOUT
    }

    override fun type(roomSettingFraudAlertUiModel: RoomSettingFraudAlertUiModel): Int {
        return RoomSettingFraudAlertViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel): Int {
        return TopchatEmptyViewHolder.LAYOUT
    }

    override fun type(productCarouselUiModel: TopChatRoomProductCarouselUiModel): Int {
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

    override fun type(broadCastUiModel: TopChatRoomBroadcastUiModel): Int {
        return when (TopChatRoomMessageTypeEnum.fromValue(broadCastUiModel.messageType)) {
            TopChatRoomMessageTypeEnum.PROMO_V2 -> {
                TopChatRoomBroadcastPromoViewHolder.LAYOUT
            }
            TopChatRoomMessageTypeEnum.FLASH_SALE_V2 -> {
                TopChatRoomBroadcastFlashSaleViewHolder.LAYOUT
            }
            else -> {
                BroadcastViewHolder.LAYOUT
            }
        }
    }

    override fun type(reviewUiModel: ReviewUiModel): Int {
        return ReviewViewHolder.LAYOUT
    }

    override fun type(srwBubbleUiModel: SrwBubbleUiModel): Int {
        return SrwBubbleViewHolder.LAYOUT
    }

    override fun type(getReminderTickerUiModel: ReminderTickerUiModel): Int {
        return ReminderTickerViewHolder.LAYOUT
    }

    override fun type(productAttachmentUiModel: ProductAttachmentUiModel): Int {
        return TopchatProductAttachmentViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingMoreModel): Int {
        return TopchatLoadingMoreViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return TopchatLoadingModelViewHolder.LAYOUT
    }

    override fun type(imageAnnouncementUiModel: ImageAnnouncementUiModel): Int {
        return TopchatImageAnnouncementViewHolder.LAYOUT
    }

    override fun type(bannedAttachmentUiModel: BannedProductAttachmentUiModel): Int {
        return TopchatBannedProductAttachmentViewHolder.LAYOUT
    }

    override fun type(multipleProductBundlingUiModel: MultipleProductBundlingUiModel): Int {
        return ProductBundlingCarouselViewHolder.LAYOUT
    }

    override fun type(productBundlingUiModel: ProductBundlingUiModel): Int {
        return ProductBundlingCardViewHolder.LAYOUT_SINGLE
    }

    override fun type(autoReplyMessageUiModel: AutoReplyMessageUiModel): Int {
        return TopChatRoomAutoReplyViewHolder.LAYOUT
    }

    override fun type(orderCancellationUiModel: TopChatRoomOrderCancellationUiModel): Int {
        return TopChatRoomOrderCancellationViewHolder.LAYOUT
    }

    // Check if chat bubble first, if not return default ViewHolder
    override fun createViewHolder(
        parent: ViewGroup,
        type: Int,
        productCarouselListListener: ProductCarouselListAttachmentViewHolder.Listener,
        productBundlingCarouselListener: ProductBundlingCarouselViewHolder.Listener,
        adapterListener: AdapterListener
    ): AbstractViewHolder<*> {
        val layoutRes = when (type) {
            TopChatRoomBubbleMessageViewHolder.TYPE_BANNED -> BannedChatMessageViewHolder.LAYOUT
            else -> type
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return createViewHolder(
            view,
            layoutRes,
            productCarouselListListener,
            productBundlingCarouselListener,
            adapterListener
        )
    }

    private fun createViewHolder(
        parent: View,
        type: Int,
        productCarouselListListener: ProductCarouselListAttachmentViewHolder.Listener,
        productBundlingCarouselListener: ProductBundlingCarouselViewHolder.Listener,
        adapterListener: AdapterListener
    ): AbstractViewHolder<*> {
        return when (type) {
            ProductCarouselListAttachmentViewHolder.LAYOUT -> ProductCarouselListAttachmentViewHolder(
                parent,
                productAttachmentListener,
                productCarouselListListener,
                deferredAttachment,
                searchListener,
                commonListener,
                adapterListener
            )
            BroadcastViewHolder.LAYOUT -> BroadcastViewHolder(
                parent, imageAnnouncementListener, voucherListener, productAttachmentListener,
                productCarouselListListener, deferredAttachment, searchListener,
                commonListener, adapterListener, chatLinkHandlerListener,
                productBundlingListener, productBundlingCarouselListener
            )
            TopChatRoomBroadcastFlashSaleViewHolder.LAYOUT -> TopChatRoomBroadcastFlashSaleViewHolder(
                parent,
                deferredAttachment,
                bannerBroadcastListener,
                productBroadcastListener,
                voucherListener
            )
            TopChatRoomBroadcastPromoViewHolder.LAYOUT -> TopChatRoomBroadcastPromoViewHolder(
                parent,
                deferredAttachment,
                bannerBroadcastListener,
                productBroadcastListener,
                voucherListener
            )
            TopChatRoomBubbleMessageViewHolder.LAYOUT -> TopChatRoomBubbleMessageViewHolder(
                parent,
                chatLinkHandlerListener,
                commonListener,
                adapterListener,
                chatMsgListener,
                replyBubbleListener
            )
            BannedChatMessageViewHolder.LAYOUT -> BannedChatMessageViewHolder(
                parent,
                censorListener,
                adapterListener
            )
            TopchatProductAttachmentViewHolder.LAYOUT -> TopchatProductAttachmentViewHolder(
                parent,
                productAttachmentListener,
                deferredAttachment,
                searchListener,
                commonListener,
                adapterListener
            )
            ReviewViewHolder.LAYOUT -> ReviewViewHolder(
                parent,
                reviewListener,
                deferredAttachment,
                adapterListener
            )
            SrwBubbleViewHolder.LAYOUT -> SrwBubbleViewHolder(
                parent,
                srwBubbleListener,
                adapterListener
            )
            ProductBundlingCarouselViewHolder.LAYOUT -> ProductBundlingCarouselViewHolder(
                parent,
                productBundlingListener,
                adapterListener,
                productBundlingCarouselListener,
                searchListener,
                commonListener,
                deferredAttachment
            )
            ProductBundlingCardViewHolder.LAYOUT_SINGLE -> ProductBundlingCardViewHolder(
                parent,
                productBundlingListener,
                adapterListener,
                searchListener,
                commonListener,
                deferredAttachment
            )
            TopChatRoomAutoReplyViewHolder.LAYOUT -> TopChatRoomAutoReplyViewHolder(
                parent,
                chatLinkHandlerListener,
                commonListener,
                adapterListener,
                chatMsgListener,
                replyBubbleListener
            )
            TopChatRoomOrderCancellationViewHolder.LAYOUT -> TopChatRoomOrderCancellationViewHolder(
                parent,
                adapterListener,
                orderCancellationListener
            )
            else -> createViewHolder(parent, type)
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ReminderTickerViewHolder.LAYOUT -> ReminderTickerViewHolder(
                parent,
                listener
            )
            TopchatBannedProductAttachmentViewHolder.LAYOUT -> TopchatBannedProductAttachmentViewHolder(
                parent,
                productAttachmentListener
            )
            TopchatImageAnnouncementViewHolder.LAYOUT -> TopchatImageAnnouncementViewHolder(
                parent,
                imageAnnouncementListener
            )
            BroadcastSpamHandlerViewHolder.LAYOUT -> BroadcastSpamHandlerViewHolder(
                parent,
                broadcastHandlingListener
            )
            TopchatLoadingModelViewHolder.LAYOUT -> TopchatLoadingModelViewHolder(parent)
            TopchatLoadingMoreViewHolder.LAYOUT -> TopchatLoadingMoreViewHolder(parent)
            StickerMessageViewHolder.LAYOUT -> StickerMessageViewHolder(
                parent,
                replyBubbleListener
            )
            HeaderDateViewHolder.LAYOUT -> HeaderDateViewHolder(parent)
            ProductAttachmentViewHolder.LAYOUT -> TopchatOldProductAttachmentViewHolder(
                parent,
                productAttachmentListener
            )
            TopchatEmptyViewHolder.LAYOUT -> TopchatEmptyViewHolder(parent)
            RoomSettingBannerViewHolder.LAYOUT -> RoomSettingBannerViewHolder(parent)
            RoomSettingFraudAlertViewHolder.LAYOUT -> RoomSettingFraudAlertViewHolder(
                parent,
                fraudAlertListener
            )
            TopchatImageUploadViewHolder.LAYOUT -> TopchatImageUploadViewHolder(
                parent,
                imageUploadListener,
                replyBubbleListener,
                commonListener,
                userSession
            )
            ImageDualAnnouncementViewHolder.LAYOUT -> ImageDualAnnouncementViewHolder(
                parent,
                imageDualAnnouncementListener
            )
            TopChatOldVoucherViewHolder.LAYOUT -> TopChatOldVoucherViewHolder(parent, voucherListener)
            AttachedInvoiceViewHolder.LAYOUT -> AttachedInvoiceViewHolder(
                parent,
                invoiceThumbnailListener,
                deferredAttachment
            )
            else -> super.createViewHolder(parent, type)
        }
    }
}
