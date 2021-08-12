package com.tokopedia.minicart.chatlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatProductUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartChatProductBinding
import com.tokopedia.utils.currency.CurrencyFormatUtil

class MiniCartChatProductViewHolder(
    private val viewBinding: ItemMiniCartChatProductBinding,
    private val chatProductListener: ChatProductListener? = null
) : AbstractViewHolder<MiniCartChatProductUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_chat_product
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
            tpPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productPrice, false)
        }
    }

    private fun setProductDisabled(element: MiniCartChatProductUiModel) {
        with(viewBinding) {
            if (element.isProductDisabled) {
                iuProduct.alpha = 0.5f
                tpTitle.alpha = 0.5f
                tpPrice.alpha = 0.5f
                divider.alpha = 0.5f
                cbProduct.hide()
            } else {
                if (!element.isChecked && element.size == 3) {
                    iuProduct.alpha = 0.5f
                    tpTitle.alpha = 0.5f
                    tpPrice.alpha = 0.5f
                    divider.alpha = 0.5f
                    cbProduct.alpha = 0.5f
                    cbProduct.show()
                    cbProduct.isChecked = element.isChecked
                    cbProduct.isEnabled = false
                    containerProduct.isEnabled = false
                } else {
                    iuProduct.alpha = 1f
                    tpTitle.alpha = 1f
                    tpPrice.alpha = 1f
                    divider.alpha = 1f
                    cbProduct.alpha = 1f
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