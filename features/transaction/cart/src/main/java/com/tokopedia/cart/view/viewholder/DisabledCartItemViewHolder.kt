package com.tokopedia.cart.view.viewholder

import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.HolderItemCartErrorBinding
import com.tokopedia.cart.domain.model.cartlist.ActionData
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_CHECKOUTBROWSER
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_DELETE
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_FOLLOWSHOP
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_SIMILARPRODUCT
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_WISHLIST
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_WISHLISTED
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.DisabledCartItemHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class DisabledCartItemViewHolder(private val binding: HolderItemCartErrorBinding, val actionListener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

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
        with(binding) {
            tvProductName.text = data.productName
            textProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.productPrice, false).removeDecimalSuffix()
            iuImageProduct.loadImage(data.productImage)
            if (data.data?.originData?.variant?.isNotBlank() == true) {
                textProductVariant.text = data.data?.originData?.variant
                textProductVariant.show()
            } else {
                textProductVariant.gone()
            }
            renderSlashPrice(data)

            data.data?.let { cartItemData ->
                tvProductName.setOnClickListener { actionListener?.onDisabledCartItemProductClicked(cartItemData) }
                iuImageProduct.setOnClickListener { actionListener?.onDisabledCartItemProductClicked(cartItemData) }
            }
        }
    }

    private fun renderSlashPrice(data: DisabledCartItemHolderData) {
        with(binding) {
            if (data.data?.originData?.priceOriginal != 0L) {
                var hasSlashPrice = false
                if (data.data?.originData?.slashPriceLabel?.isNotBlank() == true) {
                    textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.data?.originData?.priceOriginal
                            ?: 0, false).removeDecimalSuffix()
                    labelSlashPricePercentage.text = data.data?.originData?.slashPriceLabel
                    hasSlashPrice = true
                }

                if (hasSlashPrice) {
                    textSlashPrice.paintFlags = textSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    textSlashPrice.show()
                    labelSlashPricePercentage.show()
                    textProductPrice.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_4), 0, 0, 0)
                } else {
                    textSlashPrice.gone()
                    labelSlashPricePercentage.gone()
                    textProductPrice.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_16), 0, 0, 0)
                }
            } else {
                textSlashPrice.gone()
                labelSlashPricePercentage.gone()
                textProductPrice.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_16), 0, 0, 0)
            }
        }
    }

    private fun renderDefaultActionState() {
        binding.textMoveToWishlist.gone()
        binding.btnDeleteCart.invisible()
        binding.tvProductUnavailableAction.gone()
    }

    private fun renderProductAction(data: DisabledCartItemHolderData) {
        if (data.actionsData.isNotEmpty()) {
            data.actionsData.forEach {
                when (it.id) {
                    ACTION_WISHLIST, ACTION_WISHLISTED -> {
                        renderActionWishlist(it, data)
                    }
                    ACTION_CHECKOUTBROWSER, ACTION_SIMILARPRODUCT, ACTION_FOLLOWSHOP -> {
                        when {
                            data.selectedUnavailableActionId == ACTION_CHECKOUTBROWSER && it.id == ACTION_CHECKOUTBROWSER -> {
                                renderActionCheckoutInBrowser(it, data)
                            }
                            data.selectedUnavailableActionId == ACTION_SIMILARPRODUCT && it.id == ACTION_SIMILARPRODUCT -> {
                                renderActionSimilarProduct(it, data)
                            }
                            data.selectedUnavailableActionId == ACTION_FOLLOWSHOP && it.id == ACTION_FOLLOWSHOP -> {
                                renderFollowShop(it, data)
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
        binding.btnDeleteCart.apply {
            setOnClickListener {
                data.data?.let {
                    actionListener?.onDeleteDisabledItem(data)
                }
            }
            show()
        }
    }

    private fun renderFollowShop(actionData: ActionData, data: DisabledCartItemHolderData) {
        binding.tvProductUnavailableAction.apply {
            text = actionData.message
            setOnClickListener {
                data.data?.originData?.shopId?.let {
                    if (it.isNotEmpty()) {
                        actionListener?.onFollowShopClicked(it, data.errorType)
                    }
                }
            }
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            show()
        }
    }

    private fun renderActionSimilarProduct(actionData: ActionData, data: DisabledCartItemHolderData) {
        binding.tvProductUnavailableAction.apply {
            text = actionData.message
            setOnClickListener {
                if (data.selectedUnavailableActionLink.isNotBlank()) {
                    actionListener?.onSimilarProductUrlClicked(data.selectedUnavailableActionLink)
                }
            }
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            actionListener?.onShowActionSeeOtherProduct(data.productId, data.errorType)
            show()
        }
    }

    private fun renderActionCheckoutInBrowser(actionData: ActionData, data: DisabledCartItemHolderData) {
        binding.tvProductUnavailableAction.apply {
            text = actionData.message
            setOnClickListener {
                if (data.selectedUnavailableActionLink.isNotBlank()) {
                    actionListener?.onTobaccoLiteUrlClicked(data.selectedUnavailableActionLink, data, actionData)
                }
            }
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            actionListener?.onShowTickerTobacco()
            show()
        }
    }

    private fun renderActionWishlist(actionData: ActionData, data: DisabledCartItemHolderData) {
        binding.textMoveToWishlist.apply {
            if (data.isWishlisted && actionData.id == ACTION_WISHLISTED) {
                text = actionData.message
                setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
                setOnClickListener { }
            } else if (!data.isWishlisted && actionData.id == ACTION_WISHLIST) {
                text = actionData.message
                setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                setOnClickListener {
                    binding.iuImageProduct.let {
                        actionListener?.onAddDisabledItemToWishlist(data, it)
                    }
                }
            }
            show()
        }
    }

    private fun renderDivider(data: DisabledCartItemHolderData) {
        showDivider = data.showDivider
        binding.groupDivider.visibility = if (data.showDivider) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}