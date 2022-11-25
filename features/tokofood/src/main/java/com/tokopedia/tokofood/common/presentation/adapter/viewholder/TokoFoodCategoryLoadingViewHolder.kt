package com.tokopedia.tokofood.common.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodCategoryLoadingStateBinding
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel

class TokoFoodCategoryLoadingViewHolder(
    itemView: View
): AbstractViewHolder<TokoFoodCategoryLoadingStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_category_loading_state
    }

    private val binding = ItemTokofoodCategoryLoadingStateBinding.bind(itemView)

    init {
        with(binding.root) {
            setBackgroundColor(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )
        }
    }

    override fun bind(element: TokoFoodCategoryLoadingStateUiModel) {}
}
