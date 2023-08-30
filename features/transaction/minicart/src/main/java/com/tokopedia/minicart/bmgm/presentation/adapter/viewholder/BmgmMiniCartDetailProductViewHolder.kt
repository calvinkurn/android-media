package com.tokopedia.minicart.bmgm.presentation.adapter.viewholder

import android.view.View
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.media.loader.loadImage
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.presentation.model.MiniCartDetailUiModel
import com.tokopedia.minicart.databinding.ItemBmgmMiniCartDetailProductBinding
import com.tokopedia.utils.view.DarkModeUtil
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartDetailProductViewHolder(
    itemView: View
) : AbstractViewHolder<MiniCartDetailUiModel.Product>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_bmgm_mini_cart_detail_product
        private const val PRODUCT_NAME_FORMAT = "<font color=\"%s\"><b>%sx</b></font> %s"
    }

    private val binding = ItemBmgmMiniCartDetailProductBinding.bind(itemView)

    override fun bind(element: MiniCartDetailUiModel.Product) {
        showProduct(element)
    }

    private fun showProduct(element: MiniCartDetailUiModel.Product) {
        with(binding) {
            val product = element.product
            val colorStr = DarkModeUtil.getDmsHexColorByIntColor(
                root.context.getResColor(unifyprinciplesR.color.Unify_NN950)
            )
            val productName = String.format(
                PRODUCT_NAME_FORMAT, colorStr, product.quantity.toString(), product.productName
            )
            tvBmgmDetailProductName.text = productName.parseAsHtml()
            tvBmgmDetailProductPrice.text = product.getProductPriceFmt()
            dividerBmgmDetailProduct.isVisible = element.isDiscountedProduct
            showProductImage(element)
        }
    }

    private fun showProductImage(element: MiniCartDetailUiModel.Product) {
        with(binding.imgBmgmDetailProduct) {
            val product = element.product
            loadImage(product.productImage)

            if (element.isDiscountedProduct) {
                val topMargin = if (element.showTopSpace) {
                    context.dpToPx(8)
                } else {
                    context.dpToPx(0)
                }
                val bottomMargin = if (element.showBottomSpace) {
                    context.dpToPx(8)
                } else {
                    context.dpToPx(0)
                }
                setMargin(marginStart, topMargin.toInt(), marginEnd, bottomMargin.toInt())
            }
        }
    }
}