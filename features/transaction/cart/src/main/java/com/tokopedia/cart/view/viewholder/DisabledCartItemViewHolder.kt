package com.tokopedia.cart.view.viewholder

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.DisabledCartItemHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.holder_item_cart_error.view.*

class DisabledCartItemViewHolder(itemView: View, val actionListener: ActionListener?) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.holder_item_cart_error

        const val REPLACE_TAG_OPEN = "<replace>"
        const val REPLACE_TAG_CLOSE = "</replace>"
        const val LINK_TAG_CLOSE = "</a>"
    }

    var showDivider: Boolean = false

    fun bind(data: DisabledCartItemHolderData) {
        renderProductInfo(data)
        renderSlashPrice(data)
        renderDeleteButton(data)
        renderWishlistButton(data)
        renderSimilarProduct(data)
        renderDivider(data)
    }

    private fun renderProductInfo(data: DisabledCartItemHolderData) {
        itemView.tv_product_name.text = data.productName
        itemView.text_product_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.productPrice, false).removeDecimalSuffix()
        itemView.iv_image_product.loadImageRounded(data.productImage)
        if (data.data?.originData?.variant?.isNotBlank() == true) {
            itemView.text_product_variant.text = data.data?.originData?.variant
            itemView.text_product_variant.show()
        } else {
            itemView.text_product_variant.gone()
        }
    }

    private fun renderSlashPrice(data: DisabledCartItemHolderData) {
        if (data.data?.originData?.priceOriginal != 0) {
            var hasSlashPrice = false
            if (data.data?.originData?.slashPriceLabel?.isNotBlank() == true) {
                itemView.text_slash_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.data?.originData?.priceOriginal
                        ?: 0, false).removeDecimalSuffix()
                itemView.label_slash_price_percentage.text = data.data?.originData?.slashPriceLabel

                hasSlashPrice = true
            }

            if (hasSlashPrice) {
                itemView.text_slash_price.paintFlags = itemView.text_slash_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                itemView.text_slash_price.show()
                itemView.label_slash_price_percentage.show()
                itemView.text_product_price.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_4), 0, 0, 0)
            } else {
                itemView.text_slash_price.gone()
                itemView.label_slash_price_percentage.gone()
                itemView.text_product_price.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_16), 0, 0, 0)
            }
        } else {
            itemView.text_slash_price.gone()
            itemView.label_slash_price_percentage.gone()
            itemView.text_product_price.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_16), 0, 0, 0)
        }
    }

    // Deprecated
    // Todo : Confirm to PO to delete
    private fun renderTickerMessage(data: DisabledCartItemHolderData) {
        itemView.ticker_message.apply {
            if (data.nicotineLiteMessageData != null) {
                val descriptionText = data.nicotineLiteMessageData!!.text
                        .replace(REPLACE_TAG_OPEN,
                                "<a href=\"${data.nicotineLiteMessageData!!.text}\">")
                        .replace(REPLACE_TAG_CLOSE, LINK_TAG_CLOSE)
                setHtmlDescription(descriptionText)
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        actionListener?.onTobaccoLiteUrlClicked(data.nicotineLiteMessageData!!.url)
                    }

                    override fun onDismiss() {}
                })
                visibility = View.VISIBLE
                post {
                    measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                    requestLayout()
                }
                actionListener?.onShowTickerTobacco()
            } else if (data.tickerMessage != null) {
                setTextDescription(data.tickerMessage!!)
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
            data.data?.let {
                actionListener?.onDeleteDisabledItem(it)
            }
        }
    }

    private fun renderWishlistButton(data: DisabledCartItemHolderData) {
        itemView.text_move_to_wishlist.apply {
            setOnClickListener {
                if (data.isWishlisted) {
                    actionListener?.onRemoveDisabledItemFromWishlist(data.productId)
                } else {
                    actionListener?.onAddDisabledItemToWishlist(data.productId)
                }
            }
        }
    }

    private fun renderSimilarProduct(data: DisabledCartItemHolderData) {
        if (data.similarProduct != null) {
            itemView.group_similar_product_on_cart_error.visibility = View.VISIBLE
            itemView.tv_similar_product_on_cart_error.text = data.similarProduct!!.text
            itemView.tv_similar_product_on_cart_error.setOnClickListener {
                actionListener?.onSimilarProductUrlClicked(data.similarProduct!!.url)
            }
            actionListener?.onShowTickerOutOfStock(data.productId)
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