package com.tokopedia.tokopedianow.recipelist.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeCountBinding
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeCountUiModel
import com.tokopedia.utils.view.binding.viewBinding

class RecipeCountViewHolder(itemView: View): AbstractViewHolder<RecipeCountUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_recipe_count
    }

    private var binding: ItemTokopedianowRecipeCountBinding? by viewBinding()

    override fun bind(recipe: RecipeCountUiModel) {
        binding?.textCount?.apply {
            text = context.getString(R.string.tokopedianow_recipe_list_count, recipe.count)
        }
    }
}