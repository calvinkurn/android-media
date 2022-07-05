package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeProductBinding
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class RecipeProductViewHolder(
    itemView: View
) : AbstractViewHolder<RecipeProductUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_product
    }

    private var binding: ItemTokopedianowRecipeProductBinding? by viewBinding()

    override fun bind(product: RecipeProductUiModel) {
        renderProductInfo(product)
        renderSlashedPrice(product)
        renderDiscountLabel(product)
        renderProductButton(product)
    }

    private fun renderProductInfo(product: RecipeProductUiModel) {
        binding?.apply {
            textName.text = product.name
            textPrice.text = product.priceFmt
            textWeight.text = product.weight
            imgProduct.loadImage(product.imageUrl)
        }
    }

    private fun renderSlashedPrice(product: RecipeProductUiModel) {
        binding?.textSlashedPrice?.apply {
            if (product.slashedPrice.isNotEmpty()) {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = product.slashedPrice
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderDiscountLabel(product: RecipeProductUiModel) {
        binding?.labelDiscount?.apply {
            if (product.discountPercentage.isNotEmpty()) {
                text = product.discountPercentage
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderProductButton(product: RecipeProductUiModel) {
        binding?.btnProductCta?.apply {
            if (product.stock == 0) {
                text = itemView.context.getString(
                    R.string.tokopedianow_stock_empty_text
                )
                buttonVariant = UnifyButton.Variant.FILLED
                isEnabled = false
            } else {
                text = itemView.context.getString(
                    R.string.tokopedianow_add_to_cart_text
                )
                buttonVariant = UnifyButton.Variant.GHOST
                isEnabled = true
            }
        }
    }
}