package com.tokopedia.purchase_platform.features.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.cart.view.ActionListener
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.DisabledCartItemHolderData
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.holder_item_cart_error.view.*

class DisabledCartItemViewHolder(itemView: View, val actionListener: ActionListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.holder_item_cart_error
    }

    var showDivider: Boolean = false

    fun bind(data: DisabledCartItemHolderData) {
        renderProductInfo(data)
        renderError(data)
        renderTickerMessage(data)
        renderDeleteButton(data)
        renderWishlistButton(data)
        renderSimilarProduct(data)
        renderDivider(data)
    }

    private fun renderProductInfo(data: DisabledCartItemHolderData) {
        itemView.tv_product_name.text = data.productName
        itemView.tv_product_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.productPrice, false)
        itemView.iv_image_product.loadImageRounded(data.productImage)
    }

    private fun renderError(data: DisabledCartItemHolderData) {
        itemView.label_product_error.apply {
            if (data.error != null) {
                text = data.error
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }
    }

    private fun renderTickerMessage(data: DisabledCartItemHolderData) {
        itemView.ticker_message.apply {
            if (data.tickerMessage != null) {
                setHtmlDescription(data.tickerMessage!!)
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        actionListener.onTickerDescriptionUrlClicked(linkUrl.toString())
                    }

                    override fun onDismiss() {
                    }
                })
                visibility = View.VISIBLE
                post {
                    measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                    requestLayout()
                }
            } else {
                visibility = View.GONE
            }
        }
    }

    private fun renderDeleteButton(data: DisabledCartItemHolderData) {
        itemView.btn_delete_cart.setOnClickListener {
            actionListener.onDeleteDisabledItem(data.data)
        }
    }

    private fun renderWishlistButton(data: DisabledCartItemHolderData) {
        itemView.img_wishlist.apply {
            if (data.isWishlisted) {
                setImageResource(R.drawable.ic_wishlist_checkout_on)
            } else {
                setImageResource(R.drawable.ic_wishlist_checkout_off)
            }
            setOnClickListener {
                if (data.isWishlisted) {
                    actionListener.onRemoveDisabledItemFromWishlist(data.productId)
                } else {
                    actionListener.onAddDisabledItemToWishlist(data.productId)
                }
            }
        }
    }

    private fun renderSimilarProduct(data: DisabledCartItemHolderData) {
        if (data.similarProduct != null) {
            itemView.group_similar_product_on_cart_error.visibility = View.VISIBLE
            itemView.tv_similar_product_on_cart_error.text = data.similarProduct!!.text
            itemView.tv_similar_product_on_cart_error.setOnClickListener {
                actionListener.onSimilarProductUrlClicked(data.similarProduct!!.url)
            }
            actionListener.onShowTickerOutOfStock(data.productId)
        } else {
            itemView.group_similar_product_on_cart_error.visibility = View.GONE
        }
    }

    private fun renderDivider(data: DisabledCartItemHolderData) {
        showDivider = data.showDivider
        itemView.group_divider.visibility = if (data.showDivider) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}