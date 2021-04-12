package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.ProductListAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ChatMessageViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ImageAnnouncementViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ProductCarouselListAttachmentViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.TopChatVoucherViewHolderBinder
import com.tokopedia.topchat.chatroom.view.custom.FlexBoxChatLayout
import com.tokopedia.topchat.chatroom.view.custom.ProductCarouselRecyclerView
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.customview.TopchatMerchantVoucherView
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.BroadCastUiModel

class BroadcastViewHolder constructor(
        itemView: View?,
        private val imageAnnouncementListener: ImageAnnouncementListener,
        private val voucherListener: TopChatVoucherListener,
        private val productListener: ProductAttachmentListener,
        private val productCarouselListener: ProductCarouselListAttachmentViewHolder.Listener,
        private val deferredAttachment: DeferredViewHolderAttachment,
        private val searchListener: SearchListener,
        private val commonListener: CommonViewHolderListener,
        private val adapterListener: AdapterListener,
        private val chatMessageListener: ChatLinkHandlerListener
) : AbstractViewHolder<BroadCastUiModel>(itemView) {

    private val broadcastContainer: LinearLayout? = itemView?.findViewById(R.id.bubble_broadcast_container)
    private val bannerView: ImageView? = itemView?.findViewById(R.id.iv_banner)
    private val voucherView: TopchatMerchantVoucherView? = itemView?.findViewById(R.id.broadcast_merchant_voucher)
    private val singleProduct: SingleProductAttachmentContainer? = itemView?.findViewById(R.id.broadcast_product)
    private val broadcastText: FlexBoxChatLayout? = itemView?.findViewById(R.id.broadcast_fx_chat)
    private val cta: LinearLayout? = itemView?.findViewById(R.id.ll_cta_container)
    private val rvProductCarousel: ProductCarouselRecyclerView? = itemView?.findViewById(R.id.rv_product_carousel)
    private val adapterProductCarousel = ProductListAdapter(
            searchListener = searchListener,
            listener = productListener,
            deferredAttachment = deferredAttachment,
            commonListener = commonListener,
            adapterListener = adapterListener,
            isUnifyBroadcast = true
    )
    private val paddingOpposite: Int by lazy(LazyThreadSafetyMode.NONE) {
        itemView?.context?.resources?.getDimension(R.dimen.dp_topchat_1)?.toInt() ?: 0
    }
    private val paddingSender: Int by lazy(LazyThreadSafetyMode.NONE) {
        itemView?.context?.resources?.getDimension(R.dimen.dp_topchat_3)?.toInt() ?: 0
    }
    private val movementMethod = ChatLinkHandlerMovementMethod(chatMessageListener)

    init {
        ProductCarouselListAttachmentViewHolderBinder.initRecyclerView(
                rvProductCarousel, adapterListener, adapterProductCarousel, productCarouselListener, this
        )
    }

    override fun bind(element: BroadCastUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (val payload = payloads[0]) {
            DeferredAttachment.PAYLOAD_DEFERRED -> {
                bindProductCarousel(element)
                bindSingleProduct(element)
            }
        }
    }

    override fun bind(element: BroadCastUiModel) {
        bindBanner(element)
        bindVoucher(element)
        bindProductCarousel(element)
        bindSingleProduct(element)
        bindMessage(element)
        bindBackground(element)
        bindCta(element)
    }

    private fun bindBanner(element: BroadCastUiModel) {
        val banner = element.banner ?: return
        if (banner.isHideBanner) {
            bannerView?.hide()
            setPaddingTop(paddingWithoutBanner)
        } else {
            bannerView?.show()
            setPaddingTop(paddingWithBanner)
            ImageAnnouncementViewHolderBinder.bindBannerImage(banner, bannerView)
            ImageAnnouncementViewHolderBinder.bindBannerClick(
                    banner, bannerView, imageAnnouncementListener
            )
            bindBannerMargin(element)
        }
    }

    private fun setPaddingTop(topPadding: Float) {
        broadcastContainer?.apply {
            post {
                setPadding(paddingLeft, topPadding.toInt(), paddingRight, paddingBottom)
            }
        }
    }

    private fun bindBannerMargin(element: BroadCastUiModel) {
        (bannerView?.layoutParams as? LinearLayout.LayoutParams)?.apply {
            val productMarginBottom = if (element.hasVoucher()) {
                itemView.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
            } else {
                itemView.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            }
            bottomMargin = productMarginBottom.toInt()
        }

    }

    private fun bindVoucher(element: BroadCastUiModel) {
        val voucher = element.voucherUiModel
        if (voucher != null) {
            voucherView?.show()
            TopChatVoucherViewHolderBinder.bindVoucherView(voucher, voucherView, voucherListener)
            TopChatVoucherViewHolderBinder.bindClick(voucher, voucherView, voucherListener)
        } else {
            voucherView?.gone()
        }
    }

    private fun bindProductCarousel(element: BroadCastUiModel) {
        val productCarousel = element.productCarousel
        if (productCarousel != null) {
            rvProductCarousel?.show()
            ProductCarouselListAttachmentViewHolderBinder.bindDeferredAttachment(productCarousel, deferredAttachment)
            ProductCarouselListAttachmentViewHolderBinder.bindProductCarousel(productCarousel, adapterProductCarousel)
            ProductCarouselListAttachmentViewHolderBinder.bindScrollState(rvProductCarousel, productCarouselListener, this)
        } else {
            rvProductCarousel?.gone()
        }
    }

    private fun bindSingleProduct(element: BroadCastUiModel) {
        val product = element.singleProduct
        if (product != null) {
            singleProduct?.show()
            singleProduct?.bindData(
                    product, adapterPosition, productListener, deferredAttachment,
                    searchListener, commonListener, adapterListener, false
            )
        } else {
            singleProduct?.gone()
        }
    }

    private fun bindMessage(element: BroadCastUiModel) {
        val message: MessageViewModel? = element.messageUiModel
        if (message != null) {
            broadcastText?.show()
            ChatMessageViewHolderBinder.bindChatMessage(message, broadcastText, movementMethod)
            ChatMessageViewHolderBinder.bindHour(message, broadcastText)
            ChatMessageViewHolderBinder.bindChatReadStatus(message, broadcastText)
        } else {
            broadcastText?.gone()
        }
    }

    private fun bindBackground(element: BroadCastUiModel) {
        if (element.isOpposite) {
            broadcastContainer?.setPadding(paddingOpposite, paddingOpposite, paddingOpposite, paddingOpposite)
            broadcastContainer?.setBackgroundResource(R.drawable.bg_broadcast_bubble_receiver)
        } else {
            broadcastContainer?.setPadding(paddingSender, paddingSender, paddingSender, paddingSender)
            broadcastContainer?.setBackgroundResource(R.drawable.bg_broadcast_bubble_sender)
        }
    }

    private fun bindCta(element: BroadCastUiModel) {
        val banner = element.banner ?: return
        ImageAnnouncementViewHolderBinder.bindBannerClick(banner, cta, imageAnnouncementListener)
    }

    companion object {
        val LAYOUT = R.layout.item_broadcast_message_bubble

        private val paddingWithBanner = 1f.toPx()
        private val paddingWithoutBanner = 6f.toPx()
    }
}