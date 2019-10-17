package com.tokopedia.purchase_platform.features.cart.view.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.cart.view.ActionListener
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartErrorItemHolderData
import kotlinx.android.synthetic.main.holder_item_cart_error.view.*

class CartErrorItemViewHolder(itemView: View, val actionListener: ActionListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.holder_item_cart_error
    }

    var showDivider: Boolean = false

    fun bind(data: CartErrorItemHolderData) {
        renderProductInfo(data)
        renderError(data)
        renderTickerMessage(data)
        renderDeleteButton(data)
        renderWishlistButton(data)
        renderSimilarProduct(data)
        renderDivider(data)
    }

    private fun renderProductInfo(data: CartErrorItemHolderData) {
        itemView.tv_product_name.text = data.productName
        itemView.tv_product_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.productPrice, false)
        itemView.iv_image_product.loadImageRounded(data.productImage)
    }

    private fun renderError(data: CartErrorItemHolderData) {
        itemView.label_product_error.apply {
            if (data.error != null) {
                text = data.error
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }
    }

    private fun renderTickerMessage(data: CartErrorItemHolderData) {
        itemView.ticker_message.apply {
            visibility = if (data.tickerMessage != null) {
                setTextDescription(data.tickerMessage!!)
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun renderDeleteButton(data: CartErrorItemHolderData) {
        itemView.btn_delete_cart.setOnClickListener {
            TODO("action listener delete product")
        }
    }

    private fun renderWishlistButton(data: CartErrorItemHolderData) {
        itemView.img_wishlist.apply {
            if (data.isWishlisted) {
                setImageResource(R.drawable.ic_wishlist_checkout_on)
            } else {
                setImageResource(R.drawable.ic_wishlist_checkout_off)
            }
            setOnClickListener {
                TODO("action listener wishlist product")
            }
        }
    }

    private fun renderSimilarProduct(data: CartErrorItemHolderData) {
        if (data.similarProduct != null) {
            itemView.group_similar_product_on_cart_error.visibility = View.VISIBLE
            itemView.tv_similar_product_on_cart_error.text = data.similarProduct!!.text
            itemView.tv_similar_product_on_cart_error.setOnClickListener {
                TODO("action listener similar product")
            }
        } else {
            itemView.group_similar_product_on_cart_error.visibility = View.GONE
        }
    }

    private fun renderDivider(data: CartErrorItemHolderData) {
        showDivider = data.showDivider
        itemView.group_divider.visibility = if (data.showDivider) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}