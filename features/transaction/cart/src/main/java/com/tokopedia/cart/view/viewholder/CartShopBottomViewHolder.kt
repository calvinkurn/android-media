package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartShopBottomBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerState
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.utils.resources.isDarkMode

class CartShopBottomViewHolder(
    private val binding: ItemCartShopBottomBinding,
    private val actionListener: ActionListener
) : RecyclerView.ViewHolder(binding.root) {

    // variable to hold identifier
    private var cartString: String = ""

    fun bindData(cartShopBottomHolderData: CartShopBottomHolderData) {
        renderAccordion(cartShopBottomHolderData)
        renderCartShopGroupTicker(cartShopBottomHolderData)
        cartString = cartShopBottomHolderData.shopData.cartString
    }

    private fun renderAccordion(cartShopBottomHolderData: CartShopBottomHolderData) {
        if (!cartShopBottomHolderData.shopData.isError && cartShopBottomHolderData.shopData.isCollapsible) {
            val showMoreWording = itemView.context.getString(R.string.label_tokonow_show_more)
            val showLessWording = itemView.context.getString(R.string.label_tokonow_show_less)
            if (cartShopBottomHolderData.shopData.isCollapsed) {
                binding.imageChevron.rotation = CHEVRON_ROTATION_0
                binding.textAccordion.text = showMoreWording
            } else {
                binding.imageChevron.rotation = CHEVRON_ROTATION_180
                binding.textAccordion.text = showLessWording
            }

            binding.layoutAccordion.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (cartShopBottomHolderData.shopData.isCollapsed) {
                        actionListener.onExpandAvailableItem(position)
                    } else {
                        actionListener.onCollapseAvailableItem(position)
                    }
                }
            }

            binding.layoutAccordion.show()
            binding.separatorAccordion.show()
        } else {
            binding.layoutAccordion.gone()
            binding.separatorAccordion.gone()
        }
    }

    private fun renderCartShopGroupTicker(cartShopBottomHolderData: CartShopBottomHolderData) {
        if (cartShopBottomHolderData.shopData.hasSelectedProduct && !cartShopBottomHolderData.shopData.isError &&
            cartShopBottomHolderData.shopData.cartShopGroupTicker.enableCartAggregator &&
            !cartShopBottomHolderData.shopData.isOverweight
        ) {
            binding.apply {
                val cartShopGroupTicker = cartShopBottomHolderData.shopData.cartShopGroupTicker
                when (cartShopGroupTicker.state) {
                    CartShopGroupTickerState.FIRST_LOAD, CartShopGroupTickerState.LOADING -> {
                        cartShopTickerText.gone()
                        cartShopTickerLeftIcon.gone()
                        cartShopTickerRightIcon.gone()
                        cartShopTickerLargeLoader.type = LoaderUnify.TYPE_LINE
                        cartShopTickerLargeLoader.show()
                        cartShopTickerSmallLoader.type = LoaderUnify.TYPE_LINE
                        cartShopTickerSmallLoader.show()
                        layoutCartShopTicker.setBackgroundColor(MethodChecker.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_BN50))
                        layoutCartShopTicker.setOnClickListener(null)
                        layoutCartShopTicker.show()
                    }
                    CartShopGroupTickerState.SUCCESS_AFFORD, CartShopGroupTickerState.SUCCESS_NOT_AFFORD -> {
                        cartShopTickerLargeLoader.gone()
                        cartShopTickerSmallLoader.gone()
                        cartShopTickerText.text = MethodChecker.fromHtml(cartShopGroupTicker.tickerText)
                        cartShopTickerText.show()
                        if (cartShopGroupTicker.leftIcon.isNotBlank() && cartShopGroupTicker.leftIconDark.isNotBlank()) {
                            if (root.context.isDarkMode()) {
                                cartShopTickerLeftIcon.setImageUrl(cartShopGroupTicker.leftIconDark)
                            } else {
                                cartShopTickerLeftIcon.setImageUrl(cartShopGroupTicker.leftIcon)
                            }
                            cartShopTickerLeftIcon.show()
                        } else {
                            cartShopTickerLeftIcon.gone()
                        }
                        if (cartShopGroupTicker.rightIcon.isNotBlank() && cartShopGroupTicker.rightIconDark.isNotBlank()) {
                            if (root.context.isDarkMode()) {
                                cartShopTickerRightIcon.setImageUrl(cartShopGroupTicker.rightIconDark)
                            } else {
                                cartShopTickerRightIcon.setImageUrl(cartShopGroupTicker.rightIcon)
                            }
                            cartShopTickerRightIcon.show()
                        } else {
                            cartShopTickerRightIcon.gone()
                        }
                        layoutCartShopTicker.setBackgroundColor(MethodChecker.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_BN50))
                        layoutCartShopTicker.setOnClickListener {
                            actionListener.onCartShopGroupTickerClicked(cartShopBottomHolderData.shopData)
                        }
                        if (!cartShopBottomHolderData.shopData.cartShopGroupTicker.hasSeenTicker) {
                            actionListener.onViewCartShopGroupTicker(cartShopBottomHolderData.shopData)
                            cartShopBottomHolderData.shopData.cartShopGroupTicker.hasSeenTicker = true
                        }
                        layoutCartShopTicker.show()
                    }
                    CartShopGroupTickerState.FAILED -> {
                        cartShopTickerLargeLoader.gone()
                        cartShopTickerSmallLoader.gone()
                        cartShopTickerText.text = MethodChecker.fromHtml(cartShopGroupTicker.errorText)
                        cartShopTickerText.show()
                        cartShopTickerLeftIcon.gone()
                        val iconColor = MethodChecker.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
                        val reloadIcon = getIconUnifyDrawable(root.context, IconUnify.RELOAD, iconColor)
                        cartShopTickerRightIcon.setImageDrawable(reloadIcon)
                        cartShopTickerRightIcon.show()
                        layoutCartShopTicker.setBackgroundColor(MethodChecker.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_RN50))
                        layoutCartShopTicker.setOnClickListener {
                            actionListener.onCartShopGroupTickerRefreshClicked(adapterPosition, cartShopBottomHolderData)
                        }
                        layoutCartShopTicker.show()
                    }
                    CartShopGroupTickerState.EMPTY -> {
                        layoutCartShopTicker.gone()
                    }
                }
                if (cartShopGroupTicker.state == CartShopGroupTickerState.FIRST_LOAD) {
                    actionListener.checkCartShopGroupTicker(cartShopBottomHolderData.shopData)
                }
            }
        } else {
            binding.layoutCartShopTicker.gone()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_cart_shop_bottom

        private const val CHEVRON_ROTATION_0 = 0f
        private const val CHEVRON_ROTATION_180 = 180f
    }
}
