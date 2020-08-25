package com.tokopedia.cart.view.viewholder

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.DisabledCartItemHolderData
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import kotlinx.android.synthetic.main.holder_item_cart_error.view.*

class DisabledCartItemViewHolder(itemView: View, val actionListener: ActionListener?) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.holder_item_cart_error
    }

    var showDivider: Boolean = false

    fun bind(data: DisabledCartItemHolderData) {
        renderProductInfo(data)
        renderSlashPrice(data)
        renderDeleteButton(data)
        renderWishlistButton(data)
        renderProductAction(data)
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

    private fun renderProductAction(data: DisabledCartItemHolderData) {
        if (data.actionsData.isNotEmpty()) {
            data.actionsData.forEach {
                // Todo : re-check action code
                if (it.code.equals("CHECKOUTBROWSER", true)) {
                    itemView.tv_product_unavailable_action.text = it.message
                    itemView.tv_product_unavailable_action.setOnClickListener {
                        data.nicotineLiteMessageData?.url?.let {
                            actionListener?.onTobaccoLiteUrlClicked(it)
                        }
                    }
                    actionListener?.onShowTickerTobacco()
                    itemView.tv_product_unavailable_action.visibility = View.VISIBLE
                } else if (it.code.equals("SIMILARPRODUCT", true)) {
                    itemView.tv_product_unavailable_action.text = it.message
                    itemView.tv_product_unavailable_action.setOnClickListener {
                        actionListener?.onSimilarProductUrlClicked(data.similarProductUrl)
                    }
                    actionListener?.onShowTickerOutOfStock(data.productId)
                    itemView.tv_product_unavailable_action.visibility = View.VISIBLE
                }
            }
        } else {
            itemView.tv_product_unavailable_action.visibility = View.GONE
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