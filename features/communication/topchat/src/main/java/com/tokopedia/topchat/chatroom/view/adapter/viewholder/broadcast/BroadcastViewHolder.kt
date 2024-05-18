package com.tokopedia.topchat.chatroom.view.adapter.viewholder.broadcast

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.util.ChatTimeConverter.getHourTime
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.MultipleProductBundlingAdapter
import com.tokopedia.topchat.chatroom.view.adapter.ProductListAdapter
import com.tokopedia.topchat.chatroom.view.adapter.util.MessageOnTouchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ProductCarouselListAttachmentViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ImageAnnouncementViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ProductBundlingViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ProductCarouselListAttachmentViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.TopChatVoucherViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopchatProductAttachmentListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.ProductBundlingCarouselViewHolder
import com.tokopedia.topchat.chatroom.view.custom.broadcast.TopChatRoomBroadcastCountdownView
import com.tokopedia.topchat.chatroom.view.custom.ProductCarouselRecyclerView
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.regular.TopChatRoomFlexBoxLayout
import com.tokopedia.topchat.chatroom.view.custom.product_bundling.ProductBundlingCardAttachmentContainer
import com.tokopedia.topchat.chatroom.view.custom.product_bundling.ProductBundlingRecyclerView
import com.tokopedia.topchat.chatroom.view.customview.TopchatMerchantVoucherView
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class BroadcastViewHolder constructor(
    itemView: View?,
    private val imageAnnouncementListener: ImageAnnouncementListener,
    private val voucherListener: TopChatRoomVoucherListener,
    private val productListener: TopchatProductAttachmentListener,
    private val productCarouselListener: ProductCarouselListAttachmentViewHolder.Listener,
    private val deferredAttachment: DeferredViewHolderAttachment,
    private val searchListener: SearchListener,
    private val commonListener: CommonViewHolderListener,
    private val adapterListener: AdapterListener,
    chatMessageListener: ChatLinkHandlerListener,
    private val productBundlingListener: ProductBundlingListener,
    private val productBundlingCarouselListener: ProductBundlingCarouselViewHolder.Listener
) : AbstractViewHolder<TopChatRoomBroadcastUiModel>(itemView) {

    private val broadcastContainer: LinearLayout? = itemView?.findViewById(
        R.id.bubble_broadcast_container
    )
    private val bannerView: ImageView? = itemView?.findViewById(R.id.iv_banner)
    private val broadcastLabel: TopChatRoomBroadcastCountdownView? = itemView?.findViewById(
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
    private val fxChat: TopChatRoomFlexBoxLayout? = itemView?.findViewById(
        R.id.broadcast_fx_chat
    )
    private val cta: ConstraintLayout? = itemView?.findViewById(R.id.ll_cta_container)
    private val ctaText: Typography? = itemView?.findViewById(R.id.topchat_cta_broadcast_tv)
    private val ctaLabel: Label? = itemView?.findViewById(R.id.topchat_cta_broadcast_label)
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

    override fun bind(element: TopChatRoomBroadcastUiModel, payloads: MutableList<Any>) {
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

    override fun bind(element: TopChatRoomBroadcastUiModel) {
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
        element: TopChatRoomBroadcastUiModel,
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

    private fun bindBanner(element: TopChatRoomBroadcastUiModel) {
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

    private fun bindBannerMargin(element: TopChatRoomBroadcastUiModel) {
        (bannerView?.layoutParams as? LinearLayout.LayoutParams)?.apply {
            val productMarginBottom = when {
                element.hasCampaignLabel() -> PRODUCT_MARGIN_BOTTOM_CAMPAIGN_LABEL
                element.hasVoucher() -> PRODUCT_MARGIN_BOTTOM_VOUCHER.dpToPx(itemView.resources.displayMetrics)
                else -> PRODUCT_MARGIN_BOTTOM_DEFAULT.dpToPx(itemView.resources.displayMetrics)
            }
            bottomMargin = productMarginBottom
        }
    }

    private fun bindVoucher(element: TopChatRoomBroadcastUiModel) {
        val voucher = element.singleVoucher
        if (voucher != null) {
            voucherView?.show()
            TopChatVoucherViewHolderBinder.bindVoucherView(voucher, voucherView)
            TopChatVoucherViewHolderBinder.bindClick(
                voucher,
                voucherView,
                voucherListener,
                TopChatVoucherViewHolderBinder.SOURCE_BROADCAST
            )
            voucherListener.onImpressionVoucher(voucher, TopChatVoucherViewHolderBinder.SOURCE_BROADCAST)
        } else {
            voucherView?.gone()
        }
    }

    private fun bindProductCarousel(element: TopChatRoomBroadcastUiModel) {
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

    private fun bindSingleProduct(element: TopChatRoomBroadcastUiModel) {
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

    private fun bindProductBundling(element: TopChatRoomBroadcastUiModel) {
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

    private fun bindMessage(element: TopChatRoomBroadcastUiModel) {
        val message: MessageUiModel? = element.messageUiModel
        if (message != null) {
            fxChat?.show()
            fxChat?.setMessageBody(message)
            fxChat?.setMessageOnTouchListener(onTouchListener)
            fxChat?.setHourTime(getHourTime(message.replyTime))
            fxChat?.bindChatReadStatus(message)
        } else {
            fxChat?.gone()
        }
    }

    private fun bindBackground(element: TopChatRoomBroadcastUiModel) {
        if (element.isOpposite) {
            broadcastContainer?.setPadding(
                paddingOpposite,
                paddingOpposite,
                paddingOpposite,
                paddingOpposite
            )
            broadcastContainer?.setBackgroundResource(R.drawable.topchat_chatroom_broadcast_background_receiver)
        } else {
            broadcastContainer?.setPadding(
                paddingSender,
                paddingSender,
                paddingSender,
                paddingSender
            )
            broadcastContainer?.setBackgroundResource(R.drawable.topchat_chatroom_broadcast_background_sender)
        }
    }

    private fun bindCta(element: TopChatRoomBroadcastUiModel) {
        val banner = element.banner ?: return
        ctaText?.let {
            var text: String? = getString(R.string.title_topchat_see_detail)
            if (doesHaveBroadcastCtaText(banner)) {
                text = banner.broadcastCtaText

                // Change text into disabled color when label show & the user is seller
                val color = if (shouldShowBroadcastCtaLabel(banner)) {
                    unifyprinciplesR.color.Unify_NN400
                } else {
                    unifyprinciplesR.color.Unify_GN500
                }
                it.setTextColor(MethodChecker.getColor(itemView.context, color))
            }
            it.text = text
        }
        ctaLabel?.let {
            if (shouldShowBroadcastCtaLabel(banner)) {
                it.text = banner.broadcastCtaLabel
                it.show()
            } else {
                it.hide()
            }
        }
        ImageAnnouncementViewHolderBinder.bindCtaClick(banner, cta, imageAnnouncementListener)
    }

    private fun shouldShowBroadcastCtaLabel(banner: ImageAnnouncementUiModel): Boolean {
        return !banner.broadcastCtaLabel.isNullOrBlank() && commonListener.isSeller()
    }

    private fun doesHaveBroadcastCtaText(banner: ImageAnnouncementUiModel): Boolean {
        return !banner.broadcastCtaText.isNullOrBlank()
    }

    companion object {
        val LAYOUT = R.layout.topchat_chatroom_broadcast_message_bubble_item

        private val paddingWithBanner = 1f.toPx()
        private val paddingWithoutBanner = 6f.toPx()
        private const val paddingWithBroadcastLabelOnly = 0f
        private const val PRODUCT_MARGIN_BOTTOM_CAMPAIGN_LABEL = 0
        private const val PRODUCT_MARGIN_BOTTOM_VOUCHER = 4
        private const val PRODUCT_MARGIN_BOTTOM_DEFAULT = 8
    }
}
