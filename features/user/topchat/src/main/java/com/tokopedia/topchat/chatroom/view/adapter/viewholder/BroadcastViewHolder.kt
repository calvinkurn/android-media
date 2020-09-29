package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.ProductListAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ImageAnnouncementViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ProductCarouselListAttachmentViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.TopChatVoucherViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.TopchatProductAttachmentViewHolderBinder
import com.tokopedia.topchat.chatroom.view.custom.ProductCarouselRecyclerView
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.customview.TopchatMerchantVoucherView
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.BroadCastUiModel

class BroadcastViewHolder constructor(
        itemView: View?,
        private val voucherListener: TopChatVoucherListener,
        private val productListener: ProductAttachmentListener,
        private val productCarouselListener: ProductCarouselListAttachmentViewHolder.Listener,
        private val deferredAttachment: DeferredViewHolderAttachment,
        private val searchListener: SearchListener,
        private val commonListener: CommonViewHolderListener,
        private val adapterListener: AdapterListener
) : AbstractViewHolder<BroadCastUiModel>(itemView) {

    private val bannerView: ImageView? = itemView?.findViewById(R.id.iv_banner)
    private val voucherView: TopchatMerchantVoucherView? = itemView?.findViewById(R.id.broadcast_merchant_voucher)
    private val singleProduct: SingleProductAttachmentContainer? = itemView?.findViewById(R.id.broadcast_product)
    private val rvProductCarousel: ProductCarouselRecyclerView? = itemView?.findViewById(R.id.rv_product_carousel)
    private val adapterProductCarousel = ProductListAdapter(
            searchListener, productListener, deferredAttachment, commonListener, adapterListener
    )

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
            is TopchatProductAttachmentViewHolder.OccState -> {
                ProductCarouselListAttachmentViewHolderBinder.bindNewOccState(adapterProductCarousel, payload)
                element.singleProduct?.let {
                    TopchatProductAttachmentViewHolderBinder.bindNewOccState(it, singleProduct)
                }
            }
        }
    }

    override fun bind(element: BroadCastUiModel) {
        bindBanner(element)
        bindVoucher(element)
        bindProductCarousel(element)
        bindSingleProduct(element)
    }

    private fun bindBanner(element: BroadCastUiModel) {
        val banner = element.banner ?: return
        ImageAnnouncementViewHolderBinder.bindBannerImage(banner, bannerView)
    }

    private fun bindVoucher(element: BroadCastUiModel) {
        val voucher = element.voucherUiModel
        if (voucher != null) {
            voucherView?.show()
            TopChatVoucherViewHolderBinder.bindVoucherView(voucher, voucherView, voucherListener)
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
            ProductCarouselListAttachmentViewHolderBinder.bindProductCarousel(productCarousel, adapterProductCarousel)
            ProductCarouselListAttachmentViewHolderBinder.bindScrollState(rvProductCarousel, productCarouselListener, this)
        } else {
            rvProductCarousel?.gone()
        }
    }

    private fun bindSingleProduct(element: BroadCastUiModel) {
        val productCarousel = element.singleProduct
        if (productCarousel != null) {
            singleProduct?.show()
            singleProduct?.bindData(
                    productCarousel, adapterPosition, productListener, deferredAttachment,
                    searchListener, commonListener, adapterListener
            )
        } else {
            singleProduct?.gone()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_broadcast_message_bubble
    }
}