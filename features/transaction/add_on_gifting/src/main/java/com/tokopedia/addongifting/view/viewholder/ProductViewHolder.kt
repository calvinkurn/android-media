package com.tokopedia.addongifting.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.databinding.ItemProductBinding
import com.tokopedia.addongifting.view.AddOnActionListener
import com.tokopedia.addongifting.view.uimodel.ProductUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class ProductViewHolder(private val viewBinding: ItemProductBinding, private val listener: AddOnActionListener)
    : AbstractViewHolder<ProductUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_product
    }

    override fun bind(element: ProductUiModel) {
        with(viewBinding) {
            if (element.isTokoCabang) {
                labelHeaderMessage.text = "Eksklusif dari Tokocabang"
                labelOtherProducts.text = "+${element.otherProductCount} Barang Lainnya"
                labelOtherProducts.show()
            } else {
                labelHeaderMessage.text = "Disediakan oleh ${element.shopName}"
                labelOtherProducts.gone()
                imageShopBadge.setImageUrl(element.shopBadgeUrl)
            }

            imageProduct.setImageUrl(element.mainProductImageUrl)
            labelProductName.text = element.mainProductName
            labelProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.mainProductPrice, false).removeDecimalSuffix()
        }
    }

}