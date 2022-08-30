package com.tokopedia.tokopedianow.recipelist.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeFilterBinding
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.RecipeChipFilterAdapter
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.RecipeChipFilterAdapterTypeFactory
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeChipFilterViewHolder.RecipeChipFilterListener
import com.tokopedia.utils.view.binding.viewBinding

class RecipeFilterViewHolder(
    itemView: View,
    recipeFilterListener: RecipeChipFilterListener
) : AbstractViewHolder<RecipeFilterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_recipe_filter
    }

    private var binding: ItemTokopedianowRecipeFilterBinding? by viewBinding()

    private val chipAdapter by lazy {
        RecipeChipFilterAdapter(
            RecipeChipFilterAdapterTypeFactory(recipeFilterListener)
        )
    }

    override fun bind(filter: RecipeFilterUiModel) {
        binding?.recyclerView?.apply {
            adapter = chipAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            chipAdapter.submitList(filter.chips)
        }
    }
}