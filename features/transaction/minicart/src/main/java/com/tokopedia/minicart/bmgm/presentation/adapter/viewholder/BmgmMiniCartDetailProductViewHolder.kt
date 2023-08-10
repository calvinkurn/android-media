package com.tokopedia.minicart.bmgm.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.presentation.model.MiniCartDetailUiModel
import com.tokopedia.minicart.databinding.ItemBmgmMiniCartDetailProductBinding

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartDetailProductViewHolder(
    itemView: View
) : AbstractViewHolder<MiniCartDetailUiModel.Product>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_bmgm_mini_cart_detail_product
        private const val PRODUCT_NAME_FORMAT = "<b>%sx</b> %s"
    }

    private val binding = ItemBmgmMiniCartDetailProductBinding.bind(itemView)

    override fun bind(element: MiniCartDetailUiModel.Product) {
        showProduct(element)
    }

    private fun showProduct(element: MiniCartDetailUiModel.Product) {
        with(binding) {
            val product = element.product
            val productName = String.format(
                PRODUCT_NAME_FORMAT, product.quantity.toString(), product.productName
            ).parseAsHtml()
            tvBmgmDetailProductName.text = productName
            tvBmgmDetailProductPrice.text = product.productPriceFmt
            imgBmgmDetailProduct.loadImage(product.productImage)

            dividerBmgmDetailProduct.isVisible = element.isDiscountedProduct
        }
    }
}