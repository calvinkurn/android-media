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
                val shopBadgeRes = getShopBadgeByTier(element.shopTier)
                if (shopBadgeRes != -1) {
                    iconShopBadge.setImage(newIconId = shopBadgeRes)
                    iconShopBadge.show()
                } else {
                    iconShopBadge.gone()
                }
            }

            imageProduct.setImageUrl(element.mainProductImageUrl)
            labelProductName.text = element.mainProductName
            labelProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.mainProductPrice, false).removeDecimalSuffix()
        }
    }

    private fun getShopBadgeByTier(tier: Int): Int {
        when (tier) {
            0 -> return IconUnify.SHOP_FILLED
            1 -> return IconUnify.BADGE_PM_FILLED
            2 -> return IconUnify.BADGE_OS_FILLED
            3 -> return IconUnify.BADGE_PMPRO_FILLED
        }

        return -1
    }
}