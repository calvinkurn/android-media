package com.tokopedia.cartrevamp.view.viewholder.now

import android.graphics.drawable.GradientDrawable
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartCollapsedProductRevampBinding
import com.tokopedia.cartrevamp.view.ActionListener
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CartCollapsedProductViewHolder(private val viewBinding: ItemCartCollapsedProductRevampBinding, val actionListener: ActionListener) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        var LAYOUT = R.layout.item_cart_collapsed_product_revamp

        private const val PRODUCT_WIDTH = 64
    }

    fun bind(cartItemHolderData: CartItemHolderData) {
        validateContainerWidth()
        renderImage(cartItemHolderData)
        renderBundlingIcon(cartItemHolderData)
        renderVariant(cartItemHolderData)
        renderPrice(cartItemHolderData)
        renderQuantity(cartItemHolderData)
    }

    private fun validateContainerWidth() {
        val layoutParams = viewBinding.containerCollapsedProduct.layoutParams
        layoutParams.width = PRODUCT_WIDTH.dpToPx(itemView.resources.displayMetrics)
        viewBinding.containerCollapsedProduct.layoutParams = layoutParams
    }

    private fun renderBundlingIcon(cartItemHolderData: CartItemHolderData) {
        if (cartItemHolderData.isBundlingItem && !cartItemHolderData.isError) {
            viewBinding.imageBundleIcon.show()
            viewBinding.imageBundleIcon.loadImage(cartItemHolderData.bundleGrayscaleIconUrl)
        } else {
            viewBinding.imageBundleIcon.gone()
        }
    }

    private fun renderPrice(cartItemHolderData: CartItemHolderData) {
        if (cartItemHolderData.isError) {
            viewBinding.textProductPrice.gone()
        } else {
            viewBinding.textProductPrice.show()
            viewBinding.textProductPrice.text = if (cartItemHolderData.isBundlingItem) {
                CurrencyFormatUtil.convertPriceValueToIdrFormat(cartItemHolderData.bundlePrice, false)
            } else {
                CurrencyFormatUtil.convertPriceValueToIdrFormat(cartItemHolderData.productPrice, false)
            }.removeDecimalSuffix()
        }
    }

    private fun renderImage(cartItemHolderData: CartItemHolderData) {
        val frameBackground = ResourcesCompat.getDrawable(
            viewBinding.root.resources,
            R.drawable.bg_cart_product_image,
            null
        )
        if (cartItemHolderData.isError) {
            val nn900Color = ResourcesCompat.getColor(
                viewBinding.root.resources,
                unifyprinciplesR.color.Unify_NN900,
                null
            )
            val nn900ColorAlpha = ColorUtils.setAlphaComponent(nn900Color, 127)
            val loadingDrawable = frameBackground as? GradientDrawable
            loadingDrawable?.mutate()
            loadingDrawable?.setColor(nn900ColorAlpha)
            viewBinding.flImageProduct.foreground = frameBackground
        } else {
            val transparentColor = ResourcesCompat.getColor(
                viewBinding.root.resources,
                android.R.color.transparent,
                null
            )
            val loadingDrawable = frameBackground as? GradientDrawable
            loadingDrawable?.mutate()
            loadingDrawable?.setColor(transparentColor)
            viewBinding.flImageProduct.foreground = frameBackground
        }
        viewBinding.imageProduct.loadImage(cartItemHolderData.productImage)
        viewBinding.imageProduct.setOnClickListener {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (cartItemHolderData.isError) {
                    actionListener.onToggleUnavailableItemAccordion()
                } else {
                    actionListener.onCollapsedProductClicked(position, cartItemHolderData)
                }
            }
        }
    }

    private fun renderQuantity(cartItemHolderData: CartItemHolderData) {
        if (cartItemHolderData.isError) {
            viewBinding.textProductQuantity.gone()
        } else {
            viewBinding.textProductQuantity.show()
            viewBinding.textProductQuantity.text = if (cartItemHolderData.isBundlingItem) {
                itemView.resources.getString(R.string.label_collapsed_product_bundle_quantity, cartItemHolderData.bundleQuantity)
            } else {
                itemView.resources.getString(R.string.label_collapsed_product_quantity, cartItemHolderData.quantity)
            }
        }
    }

    private fun renderVariant(cartItemHolderData: CartItemHolderData) {
        if (cartItemHolderData.variant.isNotBlank() && !cartItemHolderData.isError) {
            viewBinding.textVariantName.text = cartItemHolderData.variant
            viewBinding.textVariantName.show()
        } else {
            viewBinding.textVariantName.gone()
        }

        setPriceConstraint()
    }

    private fun setPriceConstraint() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(viewBinding.containerCollapsedProduct)
        if (viewBinding.textVariantName.isVisible) {
            val margin = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_2)
            constraintSet.connect(R.id.text_product_price, ConstraintSet.TOP, R.id.text_variant_name, ConstraintSet.BOTTOM, margin)
        } else {
            val margin = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_4)
            constraintSet.connect(R.id.text_product_price, ConstraintSet.TOP, R.id.text_variant_name, ConstraintSet.BOTTOM, margin)
        }
        constraintSet.applyTo(viewBinding.containerCollapsedProduct)
    }
}
