package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.MultipleProductBundlingAdapter
import com.tokopedia.topchat.chatroom.view.adapter.ProductListAdapter
import com.tokopedia.topchat.chatroom.view.adapter.util.MessageOnTouchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ChatMessageViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ImageAnnouncementViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ProductBundlingViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ProductCarouselListAttachmentViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.TopChatVoucherViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopchatProductAttachmentListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.ProductBundlingCarouselViewHolder
import com.tokopedia.topchat.chatroom.view.custom.BroadcastCampaignLabelView
import com.tokopedia.topchat.chatroom.view.custom.FlexBoxChatLayout
import com.tokopedia.topchat.chatroom.view.custom.ProductCarouselRecyclerView
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.custom.product_bundling.ProductBundlingCardAttachmentContainer
import com.tokopedia.topchat.chatroom.view.custom.product_bundling.ProductBundlingRecyclerView
import com.tokopedia.topchat.chatroom.view.customview.TopchatMerchantVoucherView
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.BroadCastUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel
import com.tokopedia.unifyprinciples.Typography

class BroadcastViewHolder constructor(
    itemView: View?,
    private val imageAnnouncementListener: ImageAnnouncementListener,
    private val voucherListener: TopChatVoucherListener,
    private val productListener: TopchatProductAttachmentListener,
    private val productCarouselListener: ProductCarouselListAttachmentViewHolder.Listener,
    private val deferredAttachment: DeferredViewHolderAttachment,
    private val searchListener: SearchListener,
    private val commonListener: CommonViewHolderListener,
    private val adapterListener: AdapterListener,
    chatMessageListener: ChatLinkHandlerListener,
    private val productBundlingListener: ProductBundlingListener,
    private val productBundlingCarouselListener: ProductBundlingCarouselViewHolder.Listener
) : AbstractViewHolder<BroadCastUiModel>(itemView) {

    private val broadcastContainer: LinearLayout? = itemView?.findViewById(
        R.id.bubble_broadcast_container
    )
    private val bannerView: ImageView? = itemView?.findViewById(R.id.iv_banner)
    private val broadcastLabel: BroadcastCampaignLabelView? = itemView?.findViewById(
        R.id.broadcast_campaign_label
    )
    private val voucherView: TopchatMerchantVoucherView? = itemView?.findViewById(
        R.id.broadcast_merchant_voucher
    )
    private val singleProduct: SingleProductAttachmentContainer? = itemView?.findViewById(
        R.id.broadcast_product
    )
    private val singleProductBundling: ProductBundlingCardAttachmentContainer? = itemView?.findViewById(
        R.id.product_bundle_card_broadcast
    )
    private val broadcastText: FlexBoxChatLayout? = itemView?.findViewById(
        R.id.broadcast_fx_chat
    )
    private val cta: LinearLayout? = itemView?.findViewById(R.id.ll_cta_container)
    private val ctaText: Typography? = itemView?.findViewById(R.id.topchat_cta_broadcast_tv)
    private val rvProductCarousel: ProductCarouselRecyclerView? = itemView?.findViewById(
        R.id.rv_product_carousel
    )
    private val rvProductBundlingCarousel: ProductBundlingRecyclerView? = itemView?.findViewById(
        R.id.rv_product_bundle_card_broadcast
    )
    private val adapterProductCarousel = ProductListAdapter(
        searchListener = searchListener,
        listener = productListener,
        deferredAttachment = deferredAttachment,
        commonListener = commonListener,
        adapterListener = adapterListener,
        isUnifyBroadcast = true
    )
    private val multipleProductBundlingAdapter = MultipleProductBundlingAdapter(
        listener = productBundlingListener,
        adapterListener = adapterListener,
        searchListener = searchListener,
        commonListener = commonListener,
        deferredAttachment = deferredAttachment
    )
    private val paddingOpposite: Int by lazy(LazyThreadSafetyMode.NONE) {
        itemView?.context?.resources?.getDimension(R.dimen.dp_topchat_1)?.toInt() ?: 0
    }
    private val paddingSender: Int by lazy(LazyThreadSafetyMode.NONE) {
        itemView?.context?.resources?.getDimension(R.dimen.dp_topchat_3)?.toInt() ?: 0
    }
    private val onTouchListener = MessageOnTouchListener(chatMessageListener)

    init {
        ProductCarouselListAttachmentViewHolderBinder.initRecyclerView(
            rvProductCarousel,
            adapterListener,
            adapterProductCarousel,
            productCarouselListener,
            this
        )
        ProductBundlingViewHolderBinder.initRecyclerView(
            rvProductBundlingCarousel,
            adapterListener,
            multipleProductBundlingAdapter,
            productBundlingCarouselListener,
            this,
            ProductBundlingCardAttachmentContainer.BundlingSource.BROADCAST_ATTACHMENT_MULTIPLE
        )
    }

    override fun bind(element: BroadCastUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (val payload = payloads[0]) {
            DeferredAttachment.PAYLOAD_DEFERRED -> {
                bindProductCarousel(element)
                bindSingleProduct(element)
                bindBanner(element)
                bindProductBundling(element)
            }
            is SingleProductAttachmentContainer.PayloadUpdateStock -> {
                updateProductStock(element, payload)
            }
        }
    }

    override fun bind(element: BroadCastUiModel) {
        bindBanner(element)
        bindVoucher(element)
        bindProductCarousel(element)
        bindSingleProduct(element)
        bindProductBundling(element)
        bindMessage(element)
        bindBackground(element)
        bindCta(element)
    }

    private fun updateProductStock(
        element: BroadCastUiModel,
        payload: SingleProductAttachmentContainer.PayloadUpdateStock
    ) {
        if (element.isSingleProduct()) {
            val product = element.singleProduct ?: return
            singleProduct?.updateStockState(product)
        } else {
            ProductCarouselListAttachmentViewHolderBinder.updateCarouselProductStock(
                adapterProductCarousel,
                payload
            )
        }
    }

    private fun bindBanner(element: BroadCastUiModel) {
        val banner = element.banner ?: return
        bindSyncBanner(banner)
        if (banner.isHideBanner) {
            bannerView?.hide()
            setPaddingTop(paddingWithoutBanner)
        } else {
            bannerView?.show()
            setPaddingTop(paddingWithBanner)
            ImageAnnouncementViewHolderBinder.bindBannerImage(banner, bannerView)
            ImageAnnouncementViewHolderBinder.bindBannerClick(
                banner,
                bannerView,
                imageAnnouncementListener
            )
            bindBannerMargin(element)
        }
        bindBroadcastLabel(banner)
    }

    private fun bindSyncBanner(banner: ImageAnnouncementUiModel) {
        if (!banner.isLoading) return
        val chatAttachments = deferredAttachment.getLoadedChatAttachments()
        val attachment = chatAttachments[banner.attachmentId] ?: return
        if (attachment is ErrorAttachment) {
            banner.syncError()
        } else {
            banner.updateData(attachment.parsedAttributes)
        }
    }

    private fun bindBroadcastLabel(banner: ImageAnnouncementUiModel) {
        broadcastLabel?.renderState(banner)
        if (broadcastLabel?.isVisible == true && bannerView?.isVisible == false) {
            setPaddingTop(paddingWithBroadcastLabelOnly)
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
            val productMarginBottom = when {
                element.hasCampaignLabel() -> 0
                element.hasVoucher() -> itemView.context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2
                )
                else -> itemView.context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3
                )
            }
            bottomMargin = productMarginBottom.toInt()
        }
    }

    private fun bindVoucher(element: BroadCastUiModel) {
        val voucher = element.voucherUiModel
        if (voucher != null) {
            voucherView?.show()
            TopChatVoucherViewHolderBinder.bindVoucherView(voucher, voucherView)
            TopChatVoucherViewHolderBinder.bindClick(
                voucher,
                voucherView,
                voucherListener,
                TopChatVoucherViewHolderBinder.SOURCE_BROADCAST
            )
            voucherListener.onVoucherSeen(voucher, TopChatVoucherViewHolderBinder.SOURCE_BROADCAST)
        } else {
            voucherView?.gone()
        }
    }

    private fun bindProductCarousel(element: BroadCastUiModel) {
        val productCarousel = element.productCarousel
        if (productCarousel != null) {
            rvProductCarousel?.show()
            ProductCarouselListAttachmentViewHolderBinder.updateParentMetaData(
                element,
                adapterPosition,
                adapterProductCarousel
            )
            ProductCarouselListAttachmentViewHolderBinder.bindDeferredAttachment(
                productCarousel,
                deferredAttachment
            )
            ProductCarouselListAttachmentViewHolderBinder.bindProductCarousel(
                productCarousel,
                adapterProductCarousel
            )
            ProductCarouselListAttachmentViewHolderBinder.bindScrollState(
                rvProductCarousel,
                productCarouselListener,
                this
            )
        } else {
            rvProductCarousel?.gone()
        }
    }

    private fun bindSingleProduct(element: BroadCastUiModel) {
        val product = element.singleProduct
        val metaData = SingleProductAttachmentContainer.ParentViewHolderMetaData(
            element,
            adapterPosition
        )
        if (product != null) {
            singleProduct?.show()
            singleProduct?.bindData(
                product, adapterPosition, productListener, deferredAttachment,
                searchListener, commonListener, adapterListener,
                false, metaData
            )
        } else {
            singleProduct?.gone()
        }
    }

    private fun bindProductBundling(element: BroadCastUiModel) {
        val productBundling = element.productBundling
        if (productBundling != null) {
            when {
                (productBundling is MultipleProductBundlingUiModel) -> {
                    bindProductBundlingRecyclerView(productBundling)
                    singleProductBundling?.gone()
                }
                (productBundling is ProductBundlingUiModel) -> {
                    bindSingleProductBundling(productBundling)
                    rvProductBundlingCarousel?.gone()
                }
            }
        } else {
            rvProductBundlingCarousel?.gone()
            singleProductBundling?.gone()
        }
    }

    private fun bindProductBundlingRecyclerView(productBundling: MultipleProductBundlingUiModel) {
        rvProductBundlingCarousel?.show()
        ProductBundlingViewHolderBinder.bindDeferredAttachment(
            productBundling,
            deferredAttachment
        )
        ProductBundlingViewHolderBinder.bindProductBundling(
            multipleProductBundlingAdapter,
            productBundling,
            ProductBundlingCardAttachmentContainer.BundlingSource.BROADCAST_ATTACHMENT_MULTIPLE
        )
        ProductBundlingViewHolderBinder.bindScrollState(
            rvProductBundlingCarousel,
            productBundlingCarouselListener,
            this
        )
    }

    private fun bindSingleProductBundling(productBundling: ProductBundlingUiModel) {
        singleProductBundling?.show()
        singleProductBundling?.bindData(
            productBundling,
            adapterPosition,
            productBundlingListener,
            adapterListener,
            searchListener,
            commonListener,
            deferredAttachment,
            ProductBundlingCardAttachmentContainer.BundlingSource.BROADCAST_ATTACHMENT_SINGLE
        )
    }

    private fun bindMessage(element: BroadCastUiModel) {
        val message: MessageUiModel? = element.messageUiModel
        if (message != null) {
            broadcastText?.show()
            ChatMessageViewHolderBinder.bindChatMessage(message, broadcastText)
            ChatMessageViewHolderBinder.bindOnTouchMessageListener(broadcastText, onTouchListener)
            ChatMessageViewHolderBinder.bindHour(message, broadcastText)
            ChatMessageViewHolderBinder.bindChatReadStatus(message, broadcastText)
        } else {
            broadcastText?.gone()
        }
    }

    private fun bindBackground(element: BroadCastUiModel) {
        if (element.isOpposite) {
            broadcastContainer?.setPadding(
                paddingOpposite,
                paddingOpposite,
                paddingOpposite,
                paddingOpposite
            )
            broadcastContainer?.setBackgroundResource(R.drawable.bg_broadcast_bubble_receiver)
        } else {
            broadcastContainer?.setPadding(
                paddingSender,
                paddingSender,
                paddingSender,
                paddingSender
            )
            broadcastContainer?.setBackgroundResource(R.drawable.bg_broadcast_bubble_sender)
        }
    }

    private fun bindCta(element: BroadCastUiModel) {
        val banner = element.banner ?: return
        ctaText?.let {
            var text: String? = banner.broadcastCtaText
            if (text == null) {
                text = getString(R.string.title_topchat_see_detail)
            }
            it.text = text
        }
        ImageAnnouncementViewHolderBinder.bindCtaClick(banner, cta, imageAnnouncementListener)
    }

    companion object {
        val LAYOUT = R.layout.item_broadcast_message_bubble

        private val paddingWithBanner = 1f.toPx()
        private val paddingWithoutBanner = 6f.toPx()
        private val paddingWithBroadcastLabelOnly = 0f
    }
}
