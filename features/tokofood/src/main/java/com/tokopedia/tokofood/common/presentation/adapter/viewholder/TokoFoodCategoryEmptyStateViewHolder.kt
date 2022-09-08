package com.tokopedia.tokofood.common.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.constants.ImageUrl
import com.tokopedia.tokofood.databinding.ItemTokofoodCategoryEmptyStateBinding
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryEmptyStateUiModel

class TokoFoodCategoryEmptyStateViewHolder(
    view: View
) : AbstractViewHolder<TokoFoodCategoryEmptyStateUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_category_empty_state
    }

    private val binding = ItemTokofoodCategoryEmptyStateBinding.bind(itemView)

    override fun bind(element: TokoFoodCategoryEmptyStateUiModel?) {
        with(binding) {
            categoryEmptyState.emptyStateImageID.loadImage(ImageUrl.Category.IV_CATEGORY_EMPTY_STATE_URL)
        }
    }
}