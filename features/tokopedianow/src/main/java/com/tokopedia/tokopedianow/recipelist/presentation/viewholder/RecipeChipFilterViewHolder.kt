package com.tokopedia.tokopedianow.recipelist.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeListChipFilterBinding
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipFilterUiModel.ChipType.MORE_FILTER
import com.tokopedia.utils.view.binding.viewBinding

class RecipeChipFilterViewHolder(
    itemView: View,
    private val listener: RecipeChipFilterListener
) : AbstractViewHolder<RecipeChipFilterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_recipe_list_chip_filter
    }

    private var binding: ItemTokopedianowRecipeListChipFilterBinding? by viewBinding()

    override fun bind(chip: RecipeChipFilterUiModel) {
        renderText(chip)
        renderIcon(chip)
        setClickListener(chip)
    }

    private fun renderText(chip: RecipeChipFilterUiModel) {
        binding?.chip?.chipText = chip.title
    }

    private fun renderIcon(chip: RecipeChipFilterUiModel) {
        if (chip.type == MORE_FILTER) {
            binding?.chip?.apply {
                val resId = com.tokopedia.iconunify.R.drawable.iconunify_sort_filter
                val color = com.tokopedia.iconunify.R.color.Unify_NN900
                val icon = ContextCompat.getDrawable(context, resId)
                icon?.setTint(ContextCompat.getColor(context, color))
                chipImageResource = icon
            }
        }
    }

    private fun setClickListener(chip: RecipeChipFilterUiModel) {
        binding?.root?.setOnClickListener {
            listener.onClickItem(chip)
        }
    }

    interface RecipeChipFilterListener {
        fun onClickItem(filter: RecipeChipFilterUiModel)
    }
}