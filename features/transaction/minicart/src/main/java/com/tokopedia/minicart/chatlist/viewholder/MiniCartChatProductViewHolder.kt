package com.tokopedia.minicart.chatlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatProductUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartChatProductBinding
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class MiniCartChatProductViewHolder(
    private val viewBinding: ItemMiniCartChatProductBinding,
    private val chatProductListener: ChatProductListener? = null
) : AbstractViewHolder<MiniCartChatProductUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_chat_product
        private const val HALF_ALPHA = 0.5f
        private const val FULL_ALPHA = 1f
        private const val MAX_SIZE = 3
    }

    override fun bind(element: MiniCartChatProductUiModel) {
        renderProduct(element)
    }

    private fun renderProduct(element: MiniCartChatProductUiModel) {
        with(viewBinding) {
            setProductDisabled(element)
            setImage(element)
            setCheckBox(element)
            setContainer(element)
            tpTitle.text = element.productName
            tpPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productPrice, false).removeDecimalSuffix()
        }
    }

    private fun setProductDisabled(element: MiniCartChatProductUiModel) {
        with(viewBinding) {
            val alpha: Float
            if (element.isProductDisabled) {
                alpha = HALF_ALPHA
                iuProduct.alpha = alpha
                tpTitle.alpha = alpha
                tpPrice.alpha = alpha
                divider.alpha = alpha
                cbProduct.hide()
            } else {
                if (!element.isChecked && element.size == MAX_SIZE) {
                    alpha = HALF_ALPHA
                    iuProduct.alpha = alpha
                    tpTitle.alpha = alpha
                    tpPrice.alpha = alpha
                    divider.alpha = alpha
                    cbProduct.alpha = alpha
                    cbProduct.show()
                    cbProduct.isChecked = element.isChecked
                    cbProduct.isEnabled = false
                    containerProduct.isEnabled = false
                } else {
                    alpha = FULL_ALPHA
                    iuProduct.alpha = alpha
                    tpTitle.alpha = alpha
                    tpPrice.alpha = alpha
                    divider.alpha = alpha
                    cbProduct.alpha = alpha
                    cbProduct.show()
                    cbProduct.isChecked = element.isChecked
                    cbProduct.isEnabled = true
                    containerProduct.isEnabled = true
                }
            }
        }
    }

    private fun setImage(element: MiniCartChatProductUiModel) {
        with(viewBinding) {
            if (element.productImageUrl.isNotBlank()) {
                ImageHandler.loadImageWithoutPlaceholder(iuProduct, element.productImageUrl)
            }
        }
    }

    private fun setCheckBox(element: MiniCartChatProductUiModel) {
        with(viewBinding) {
            cbProduct.setOnClickListener {
                element.isChecked = cbProduct.isChecked
                chatProductListener?.onClickProduct(element, element.isChecked)
            }
        }
    }

    private fun setContainer(element: MiniCartChatProductUiModel) {
        with(viewBinding) {
            containerProduct.setOnClickListener {
                if (cbProduct.isShown) {
                    cbProduct.isChecked = !cbProduct.isChecked
                    element.isChecked = cbProduct.isChecked
                    chatProductListener?.onClickProduct(element, element.isChecked)
                }
            }
        }
    }

    interface ChatProductListener {
        fun onClickProduct(element: MiniCartChatProductUiModel, isChecked: Boolean)
    }
}
