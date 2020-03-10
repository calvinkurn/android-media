package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.topchat.R
import kotlinx.android.synthetic.main.item_topchat_product_attachment.view.*

class TopchatProductAttachmentViewHolder(
        itemView: View?,
        private val listener: ProductAttachmentListener
) : BaseChatViewHolder<ProductAttachmentViewModel>(itemView) {

    override fun alwaysShowTime(): Boolean {
        return true
    }

    override fun bind(product: ProductAttachmentViewModel?) {
        if (product == null) return
        super.bind(product)
        bindProductClick(product)
        bindImage(product)
        bindName(product)
        bindCampaign(product)
        bindPrice(product)
        bindFreeShipping(product)
        bindBuy(product)
        bindAtc(product)
        bindChatReadStatus(product)
        listener.trackSeenProduct(product)
    }

    private fun bindProductClick(product: ProductAttachmentViewModel) {
        itemView.setOnClickListener { listener.onProductClicked(product) }
    }

    private fun bindImage(product: ProductAttachmentViewModel) {
        ImageHandler.loadImageRounded2(
                itemView.context,
                itemView.iv_thumbnail,
                product.productImage,
                8f.toPx()
        )
    }

    private fun bindName(product: ProductAttachmentViewModel) {
        itemView.tv_product_name?.text = product.productName
    }

    private fun bindCampaign(product: ProductAttachmentViewModel) {
        if (product.hasDiscount) {
            toggleCampaign(View.VISIBLE)
            bindDiscount(product)
            bindDropPrice(product)
        } else {
            toggleCampaign(View.GONE)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindDiscount(product: ProductAttachmentViewModel) {
        itemView.tv_campaign_discount?.text = "${product.dropPercentage}%"
    }

    private fun bindDropPrice(product: ProductAttachmentViewModel) {
        itemView.tv_campaign_price?.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text = product.priceBefore
        }
    }

    private fun bindPrice(product: ProductAttachmentViewModel) {
        itemView.tv_price?.text = product.productPrice
    }

    private fun bindFreeShipping(product: ProductAttachmentViewModel) {
        if (product.hasFreeShipping()) {
            itemView.iv_free_shipping?.show()
            ImageHandler.loadImageRounded2(itemView.context, itemView.iv_free_shipping, product.getFreeShippingImageUrl())
        } else {
            itemView.iv_free_shipping?.hide()
        }
    }

    private fun bindBuy(product: ProductAttachmentViewModel) {
        if (product.isSender) {
            itemView.tv_buy?.show()
            itemView.tv_buy?.setOnClickListener {
                listener.onClickBuyFromProductAttachment(product)
            }
        } else {
            itemView.tv_buy?.hide()
        }
    }

    private fun bindAtc(product: ProductAttachmentViewModel) {
        if (product.isSender) {
            itemView.tv_atc?.show()
            itemView.tv_atc?.setOnClickListener {
                listener.onClickATCFromProductAttachment(product)
            }
        } else {
            itemView.tv_atc?.hide()
        }
    }

    private fun toggleCampaign(visibility: Int) {
        with(itemView) {
            tv_campaign_discount?.visibility = visibility
            tv_campaign_price?.visibility = visibility
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_product_attachment
    }
}