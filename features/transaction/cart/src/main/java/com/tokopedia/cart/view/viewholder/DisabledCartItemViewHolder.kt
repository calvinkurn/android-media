package com.tokopedia.cart.view.viewholder

import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.domain.model.cartlist.ActionData
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_CHECKOUTBROWSER
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_DELETE
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_SIMILARPRODUCT
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_WISHLIST
import com.tokopedia.cart.view.*
import com.tokopedia.cart.view.uimodel.DisabledCartItemHolderData
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
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
        renderDefaultActionState()
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
        renderSlashPrice(data)

        data.data?.let { cartItemData ->
            itemView.tv_product_name.setOnClickListener { actionListener?.onDisabledCartItemProductClicked(cartItemData) }
            itemView.iv_image_product.setOnClickListener { actionListener?.onDisabledCartItemProductClicked(cartItemData) }
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

    private fun renderDefaultActionState() {
        itemView.text_move_to_wishlist.gone()
        itemView.btn_delete_cart.invisible()
        itemView.tv_product_unavailable_action.gone()
    }

    private fun renderProductAction(data: DisabledCartItemHolderData) {
        if (data.actionsData.isNotEmpty()) {
            data.actionsData.forEach {
                when (it.id) {
                    ACTION_WISHLIST -> {
                        renderActionWishlist(it, data)
                    }
                    ACTION_CHECKOUTBROWSER, ACTION_SIMILARPRODUCT -> {
                        when {
                            data.selectedUnavailableActionId == ACTION_CHECKOUTBROWSER && it.id == ACTION_CHECKOUTBROWSER -> {
                                renderActionCheckoutInBrowser(it, data)
                            }
                            data.selectedUnavailableActionId == ACTION_SIMILARPRODUCT && it.id == ACTION_SIMILARPRODUCT -> {
                                renderActionSimilarProduct(it, data)
                            }
                        }
                    }
                    ACTION_DELETE -> {
                        renderActionDelete(data)
                    }
                }
            }
        }
    }

    private fun renderActionDelete(data: DisabledCartItemHolderData) {
        itemView.btn_delete_cart.setOnClickListener {
            data.data?.let {
                actionListener?.onDeleteDisabledItem(data)
            }
        }
        itemView.btn_delete_cart.show()
    }

    private fun renderActionSimilarProduct(it: ActionData, data: DisabledCartItemHolderData) {
        itemView.tv_product_unavailable_action.text = it.message
        itemView.tv_product_unavailable_action.setOnClickListener {
            actionListener?.onSimilarProductUrlClicked(data.similarProductUrl)
        }
        actionListener?.onShowActionSeeOtherProduct(data.productId, data.errorType)
        itemView.tv_product_unavailable_action.show()
    }

    private fun renderActionCheckoutInBrowser(actionData: ActionData, data: DisabledCartItemHolderData) {
        itemView.tv_product_unavailable_action.text = actionData.message
        itemView.tv_product_unavailable_action.setOnClickListener {
            data.nicotineLiteMessageData?.url?.let {
                actionListener?.onTobaccoLiteUrlClicked(it, data, actionData)
            }
        }
        actionListener?.onShowTickerTobacco()
        itemView.tv_product_unavailable_action.show()
    }

    private fun renderActionWishlist(it: ActionData, data: DisabledCartItemHolderData) {
        if (data.isWishlisted) {
            itemView.text_move_to_wishlist.text = "Sudah ada di Wishlist"
            itemView.text_move_to_wishlist.setTextColor(ContextCompat.getColor(itemView.context, R.color.Neutral_N700_32))
            itemView.text_move_to_wishlist.setOnClickListener { }
        } else {
            itemView.text_move_to_wishlist.text = it.message
            itemView.text_move_to_wishlist.setTextColor(ContextCompat.getColor(itemView.context, R.color.Neutral_N700_68))
            itemView.text_move_to_wishlist.setOnClickListener {
                actionListener?.onAddDisabledItemToWishlist(data)
            }

        }

        itemView.text_move_to_wishlist.show()
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