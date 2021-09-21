package com.tokopedia.orderhistory.view.adapter.viewholder

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.orderhistory.R
import com.tokopedia.orderhistory.data.Product
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class OrderHistoryViewHolder(
        itemView: View?,
        private val listener: Listener? = null
) : AbstractViewHolder<Product>(itemView) {

    interface Listener {
        fun onClickBuyAgain(product: Product)
        fun onClickAddToWishList(product: Product)
        fun onClickCardProduct(product: Product, position: Int)
        fun trackSeenProduct(product: Product, position: Int)
    }

    private var thumbnail: ImageView? = itemView?.findViewById(R.id.iv_thumbnail)
    private var emptyStock: Label? = itemView?.findViewById(R.id.lb_empty_stock)
    private var productName: Typography? = itemView?.findViewById(R.id.tv_product_name)
    private var campaignDiscount: Label? = itemView?.findViewById(R.id.tv_campaign_discount)
    private var campaignPreviousPrice: Typography? = itemView?.findViewById(R.id.tv_campaign_price)
    private var finalPrice: Typography? = itemView?.findViewById(R.id.tv_price)
    private var freeShipping: ImageView? = itemView?.findViewById(R.id.iv_free_shipping)
    private var buyAgainButton: UnifyButton? = itemView?.findViewById(R.id.tv_buy)
    private var wishListButton: UnifyButton? = itemView?.findViewById(R.id.tv_wishlist)

    override fun bind(product: Product?) {
        if (product == null) return
        bindCardClick(product)
        bindImage(product)
        bindEmptyStockLabel(product)
        bindName(product)
        bindCampaign(product)
        bindPrice(product)
        bindFreeShipping(product)
        bindCtaButton(product)
        listener?.trackSeenProduct(product, adapterPosition)
    }

    private fun bindCardClick(product: Product) {
        itemView.setOnClickListener {
            listener?.onClickCardProduct(product, adapterPosition)
        }
    }

    private fun bindImage(product: Product) {
        ImageHandler.loadImageRounded2(
                itemView.context,
                thumbnail,
                product.imageUrl,
                8f.toPx()
        )
    }

    private fun bindEmptyStockLabel(product: Product) {
        emptyStock?.apply {
            if (product.hasEmptyStock) {
                show()
                unlockFeature = true
                setLabelType(context.getString(R.string.orderhistory_label_empty_stock_color))
            } else {
                hide()
            }
        }
    }

    private fun bindName(product: Product) {
        productName?.text = product.name
    }

    private fun bindCampaign(product: Product) {
        if (product.hasDiscount) {
            toggleCampaign(View.VISIBLE)
            bindDiscount(product)
            bindDropPrice(product)
        } else {
            toggleCampaign(View.GONE)
        }
    }

    private fun bindPrice(product: Product) {
        finalPrice?.text = product.price
    }

    private fun bindCtaButton(product: Product) {
        if (product.hasEmptyStock) {
            wishListButton?.show()
            buyAgainButton?.hide()
            bindClickWishList(product)
        } else {
            wishListButton?.hide()
            buyAgainButton?.show()
            bindClickBuyAgain(product)
        }
    }

    private fun bindFreeShipping(product: Product) {
        if (product.hasFreeShipping) {
            freeShipping?.show()
            ImageHandler.loadImageRounded2(itemView.context, freeShipping, product.freeShipping.imageUrl)
        } else {
            freeShipping?.hide()
        }
    }

    private fun bindClickBuyAgain(product: Product) {
        buyAgainButton?.setOnClickListener {
            listener?.onClickBuyAgain(product)
        }
    }

    private fun bindClickWishList(product: Product) {
        wishListButton?.setOnClickListener {
            listener?.onClickAddToWishList(product)
        }
    }

    private fun toggleCampaign(visibility: Int) {
        campaignDiscount?.visibility = visibility
        campaignPreviousPrice?.visibility = visibility
    }

    @SuppressLint("SetTextI18n")
    private fun bindDiscount(product: Product) {
        campaignDiscount?.text = "${product.discountedPercentage}%"
    }

    private fun bindDropPrice(product: Product) {
        campaignPreviousPrice?.apply {
            text = product.priceBefore
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    companion object {
        val LAYOUT = R.layout.item_order_history
    }
}