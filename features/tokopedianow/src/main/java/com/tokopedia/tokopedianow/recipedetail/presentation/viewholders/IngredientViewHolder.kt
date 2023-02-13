package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeIngredientBinding
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientUiModel
import com.tokopedia.utils.view.binding.viewBinding

class IngredientViewHolder(itemView: View) : AbstractViewHolder<IngredientUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_ingredient
    }

    private var binding: ItemTokopedianowRecipeIngredientBinding? by viewBinding()

    override fun bind(ingredient: IngredientUiModel) {
        binding?.apply {
            val quantity = itemView.context.resources.getString(
                R.string.tokopedianow_recipe_ingredient_quantity,
                ingredient.quantity,
                ingredient.unit
            )
            textName.text = ingredient.name
            textQuantity.text = quantity
            renderLastItem(ingredient)
        }
    }

    private fun renderLastItem(ingredient: IngredientUiModel) {
        if (ingredient.isLastItem) {
            val marginBottom = itemView.context.resources.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
            val marginZero = itemView.context.resources.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
            itemView.setMargin(marginZero, marginZero, marginZero, marginBottom)
            binding?.bottomDivider?.hide()
        }
    }
}