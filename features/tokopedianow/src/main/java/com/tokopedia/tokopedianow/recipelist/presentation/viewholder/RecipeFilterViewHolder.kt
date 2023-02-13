package com.tokopedia.tokopedianow.recipelist.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeFilterBinding
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterUiModel
import com.tokopedia.utils.view.binding.viewBinding

class RecipeFilterViewHolder(
    itemView: View,
    private val listener: RecipeChipFilterListener
) : AbstractViewHolder<RecipeFilterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_recipe_filter
    }

    private var binding: ItemTokopedianowRecipeFilterBinding? by viewBinding()

    override fun bind(filter: RecipeFilterUiModel) {
        binding?.chipFilter?.apply {
            sortFilterItems.removeAllViews()
            sortFilterHorizontalScrollView.scrollX = 0
            parentListener = {
                listener.onClickMoreFilter()
            }
            indicatorCounter = filter.selectedFiltersCount
        }
    }

    interface RecipeChipFilterListener {
        fun onClickMoreFilter()
    }
}