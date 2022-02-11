package com.tokopedia.addongifting.addonbottomsheet.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.databinding.ItemProductBinding
import com.tokopedia.addongifting.addonbottomsheet.view.AddOnActionListener
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.ProductUiModel
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
                labelHeaderMessage.text = itemView.context.getString(R.string.add_on_label_header_product_from_toko_cabang)
                labelOtherProducts.text = String.format(itemView.context.getString(R.string.add_on_label_other_product), element.otherProductCount)
                labelOtherProducts.show()
            } else {
                labelHeaderMessage.text = String.format(itemView.context.getString(R.string.add_on_label_header_product), element.shopName)
                labelOtherProducts.gone()
            }

            if (element.promoMessage.isNotBlank()) {
                iconPromo.show()
                labelPromoInfo.text = element.promoMessage
            } else {
                iconPromo.gone()
                labelPromoInfo.gone()
            }

            imageProduct.setImageUrl(element.mainProductImageUrl)
            labelProductName.text = element.mainProductName
            labelProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.mainProductPrice, false).removeDecimalSuffix()
        }
    }

}