package com.tokopedia.tokopedianow.recipelist.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeListChipBinding
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipUiModel.ChipType.MORE_FILTER
import com.tokopedia.utils.view.binding.viewBinding

class RecipeChipViewHolder(itemView: View) : AbstractViewHolder<RecipeChipUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_recipe_list_chip
    }

    private var binding: ItemTokopedianowRecipeListChipBinding? by viewBinding()

    override fun bind(chip: RecipeChipUiModel) {
        renderText(chip)
        renderIcon(chip)
    }

    private fun renderText(chip: RecipeChipUiModel) {
        binding?.chip?.chipText = chip.title
    }

    private fun renderIcon(chip: RecipeChipUiModel) {
        binding?.chip?.apply {
            when (chip.type) {
                MORE_FILTER -> {
                    val resId = com.tokopedia.iconunify.R.drawable.iconunify_sort_filter
                    val color = com.tokopedia.iconunify.R.color.Unify_NN900
                    val icon = ContextCompat.getDrawable(context, resId)
                    icon?.setTint(ContextCompat.getColor(context, color))
                    chipImageResource = icon
                }
                else -> {
                    // do nothing
                }
            }
        }
    }
}