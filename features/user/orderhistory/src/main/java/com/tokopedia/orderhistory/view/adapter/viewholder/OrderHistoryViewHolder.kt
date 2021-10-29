package com.tokopedia.orderhistory.view.adapter.viewholder

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.orderhistory.R
import com.tokopedia.orderhistory.data.Product
import com.tokopedia.orderhistory.databinding.ItemOrderHistoryBinding
import com.tokopedia.utils.view.binding.viewBinding

class OrderHistoryViewHolder(
        itemView: View?,
        private val listener: Listener? = null
) : AbstractViewHolder<Product>(itemView) {

    private val binding: ItemOrderHistoryBinding? by viewBinding()

    interface Listener {
        fun onClickBuyAgain(product: Product)
        fun onClickAddToWishList(product: Product)
        fun onClickCardProduct(product: Product, position: Int)
        fun trackSeenProduct(product: Product, position: Int)
    }

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
                binding?.ivThumbnail,
                product.imageUrl,
                8f.toPx()
        )
    }

    private fun bindEmptyStockLabel(product: Product) {
        binding?.lbEmptyStock?.apply {
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
        binding?.tvProductName?.text = product.name
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
        binding?.tvPrice?.text = product.price
    }

    private fun bindCtaButton(product: Product) {
        if (product.hasEmptyStock) {
            binding?.let { binding ->
                binding.tvWishlist.show()
                binding.tvBuy.hide()
                bindClickWishList(product)
            }
        } else {
            binding?.let { binding ->
                binding.tvWishlist.hide()
                binding.tvBuy.show()
                bindClickBuyAgain(product)
            }
        }
    }

    private fun bindFreeShipping(product: Product) {
        if (product.hasFreeShipping) {
            binding?.ivFreeShipping?.show()
            ImageHandler.loadImageRounded2(itemView.context, binding?.ivFreeShipping, product.freeShipping.imageUrl)
        } else {
            binding?.ivFreeShipping?.hide()
        }
    }

    private fun bindClickBuyAgain(product: Product) {
        binding?.tvBuy?.setOnClickListener {
            listener?.onClickBuyAgain(product)
        }
    }

    private fun bindClickWishList(product: Product) {
        binding?.tvWishlist?.setOnClickListener {
            listener?.onClickAddToWishList(product)
        }
    }

    private fun toggleCampaign(visibility: Int) {
        binding?.tvCampaignDiscount?.visibility = visibility
        binding?.tvCampaignPrice?.visibility = visibility
    }

    @SuppressLint("SetTextI18n")
    private fun bindDiscount(product: Product) {
        binding?.tvCampaignDiscount?.text = "${product.discountedPercentage}%"
    }

    private fun bindDropPrice(product: Product) {
        binding?.tvCampaignPrice?.apply {
            text = product.priceBefore
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    companion object {
        val LAYOUT = R.layout.item_order_history
    }
}